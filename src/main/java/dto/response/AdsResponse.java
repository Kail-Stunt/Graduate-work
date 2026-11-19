
package dto.response;

import java.util.List;

public class AdsResponse {

    private Integer count;
    private List<AdResponse> results;

    public AdsResponse() {}

    public AdsResponse(Integer count, List<AdResponse> results) {
        this.count = count;
        this.results = results;
    }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }

    public List<AdResponse> getResults() { return results; }
    public void setResults(List<AdResponse> results) { this.results = results; }
}
