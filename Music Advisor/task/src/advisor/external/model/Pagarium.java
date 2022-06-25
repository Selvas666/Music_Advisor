package advisor.external.model;

import io.vavr.collection.List;

public class Pagarium {
    List <List <String>> pages;
    int pageCount;
    int currentPage;

    public Pagarium(List<String> masterList, int page) {
        this.pages = masterList.grouped(page).toList();
        this.currentPage = 0;
        this.pageCount = pages.size();
    }

    private void printPage () {
        System.out.printf("---PAGE %d OF %d---%n", currentPage, pageCount);
    }

    public void printNext() {
        currentPage = currentPage + 1;
        if (currentPage > pageCount) {
            currentPage = pageCount;
            System.out.println("No more pages.");
            return;
        }
        pages.get(currentPage-1).forEach(System.out::println);
        printPage();
    }

    public void printPrev() {
        currentPage = currentPage - 1;
        if (currentPage < 1) {
            currentPage = 1;
            System.out.println("No more pages.");
            return;
        }
        pages.get(currentPage-1).forEach(System.out::println);
        printPage();
    }



}
