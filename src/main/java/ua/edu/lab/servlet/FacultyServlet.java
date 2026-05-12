package ua.edu.lab.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.edu.lab.model.*;
import ua.edu.lab.repository.UniversityRepository;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet(name = "FacultyServlet", urlPatterns = {"/faculty/*"})
public class FacultyServlet extends HttpServlet {
    private final UniversityRepository repo = new UniversityRepository();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        try {
            repo.load(getServletContext());
        } catch (IOException e) {
            throw new ServletException("Помилка завантаження даних: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            writeResponse(resp, repo.getAll());
            return;
        }

        String[] parts = path.split("/");

        if (parts.length == 2 && "all".equals(parts[1])) {
            writeResponse(resp, repo.getAll());
            return;
        }

        if (parts.length == 4 && "student".equals(parts[2]) && "all".equals(parts[3])) {
            try {
                int fId = Integer.parseInt(parts[1]);
                Faculty f = repo.getFaculty(fId).orElse(null);

                if (f != null) {
                    String acceptHeader = req.getHeader("Accept");
                    if (acceptHeader != null && acceptHeader.contains("application/json")) {
                        writeResponse(resp, f.getGroups().stream()
                                .flatMap(g -> g.getStudents().stream())
                                .collect(Collectors.toList()));
                    } else {
                        renderHtml(resp, f);
                    }
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Факультет не знайдено");
                }
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Невірний формат ID");
            }
            return;
        }

        // GET /faculty/{id}/student/{iid}
        if (parts.length == 4) {
            try {
                int fId = Integer.parseInt(parts[1]);
                int sId = Integer.parseInt(parts[3]);
                writeResponse(resp, repo.getStudent(fId, sId).orElse(null));
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Невірний формат ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] parts = path.split("/");
        // Очікуємо /faculty/{id}/student/new
        if (parts.length >= 3) {
            try {
                int fId = Integer.parseInt(parts[1]);
                Student s = mapper.readValue(req.getInputStream(), Student.class);

                repo.getFaculty(fId).ifPresentOrElse(f -> {
                    if (!f.getGroups().isEmpty()) {
                        f.getGroups().get(0).getStudents().add(s);
                        try {
                            writeResponse(resp, s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, () -> {
                    try {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Факультет не знайдено");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Помилка даних: " + e.getMessage());
            }
        }
    }

    private void writeResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(resp.getOutputStream(), data);
    }

    private void renderHtml(HttpServletResponse resp, Faculty f) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Студенти</title></head><body>");
        sb.append("<h1>Факультет: ").append(f.getName()).append("</h1>");

        if (f.getLogo() != null) {
            sb.append("<img src='").append(f.getLogo()).append("' width='100'><br>");
        }

        for (Group g : f.getGroups()) {
            sb.append("<h2>Група: ").append(g.getGroupNumber()).append("</h2>");
            sb.append("<table border='1' cellpadding='5' style='border-collapse: collapse;'>");
            sb.append("<tr style='background-color: #f2f2f2;'><th>ПІБ</th><th>Email</th><th>Квиток</th></tr>");
            for (Student s : g.getStudents()) {
                sb.append("<tr><td>").append(s.getFullName()).append("</td><td>")
                        .append(s.getEmail()).append("</td><td>")
                        .append(s.getStudentIdCard()).append("</td></tr>");
            }
            sb.append("</table>");
        }
        sb.append("</body></html>");
        resp.getWriter().write(sb.toString());
    }
}