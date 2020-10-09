package life.majiang.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer pageNo;
    private List<Integer> pages = new ArrayList<>();
    private Integer pageNum;

    private Integer max(Integer a, Integer b) {
        return a > b ? a : b;
    }

    private Integer min(Integer a, Integer b) {
        return a < b ? a : b;
    }

    public void setPagination(Integer totalCount, Integer pageNo, Integer pageListsNum) {

        setPageNo(pageNo);

        Integer numPages = 0;

        if (totalCount % pageListsNum == 0)
            numPages = totalCount / pageListsNum;
        else
            numPages = totalCount / pageListsNum + 1;

        Integer start = max(pageNo - 3, 1), end = min(pageNo + 3, numPages);

        for (Integer i = start; i <= end; i++)
            pages.add(i);

        setPageNum(pages.size());

        showPrevious = pageNo > 1;
        showNext = pageNo < numPages;

        showFirstPage = !pages.contains(1);
        showEndPage = !pages.contains(numPages);
    }
}
