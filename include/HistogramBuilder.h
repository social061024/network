#pragma once
#include <string>
#include <vector>

class HistogramBuilder {
public:
    static std::string buildHistogram(const std::vector<int>& data);
};
