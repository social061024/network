#include "HistogramBuilder.h"
#include <sstream>

std::string HistogramBuilder::buildHistogram(const std::vector<int>& data) {
    std::ostringstream oss;
    oss << "Histogram:\n";
    for (size_t i = 0; i < data.size(); ++i) {
        oss << i << ": " << std::string(data[i], '*') << "\n";
    }
    return oss.str();
}