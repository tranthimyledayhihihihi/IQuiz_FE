package com.example.iq5.feature.multiplayer.data.models;

import java.util.List;

public class MatchHistoryResponse {
    private List<MatchHistoryItem> matches;
    private int totalCount;
    private int pageNumber;
    private int pageSize;

    public MatchHistoryResponse() {}

    public List<MatchHistoryItem> getMatches() { return matches; }
    public void setMatches(List<MatchHistoryItem> matches) { this.matches = matches; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}