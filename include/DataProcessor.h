#pragma once
#include <vector>
#include <string>

class DataProcessor {
public:
    static std::vector<int> parseData(const std::string& str);
    static std::string serializeData(const std::vector<int>& data);
};
