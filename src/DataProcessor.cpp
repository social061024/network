#include "DataProcessor.h"
#include <sstream>

std::vector<int> DataProcessor::parseData(const std::string& str) {
    std::vector<int> result;
    std::istringstream iss(str);
    int num;
    while (iss >> num) {
        result.push_back(num);
    }
    return result;
}

std::string DataProcessor::serializeData(const std::vector<int>& data) {
    std::ostringstream oss;
    for (int num : data) {
        oss << num << " ";
    }
    return oss.str();
}
