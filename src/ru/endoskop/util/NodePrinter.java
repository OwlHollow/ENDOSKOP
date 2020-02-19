package ru.endoskop.util;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import ru.endoskop.Endoskop;

public class NodePrinter {
    public boolean print(Node node, PageOrientation pageOrientation){
        boolean success = false;
        
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, 
                pageOrientation,
                Printer.MarginType.HARDWARE_MINIMUM);
        
        Endoskop.getWindowManager().setSize(
            pageLayout.getPrintableWidth(),
            pageLayout.getPrintableHeight());
        Endoskop.getWindowManager()
            .getStage().getScene().getStylesheets().clear();
        Endoskop.getWindowManager()
            .getStage().getScene().getStylesheets()
            .add("ru/endoskop/style/print.css");
        
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
           job.getJobSettings().setPageLayout(pageLayout);
           success = job.printPage(node);
           if (success) {
               job.endJob();
           }
        }
        return success;
    }
}
