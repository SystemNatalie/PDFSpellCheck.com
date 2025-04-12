//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile("HB19.pdf"))) {
            PDPage page = document.getPage(0); // Get the first page

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);

            //this is for when we eventually add a text box selector. currently unused.
            PDRectangle mediaBox = page.getMediaBox();
            float width_page = mediaBox.getWidth();   // In points (pt)
            float height_page = mediaBox.getHeight(); // In points (pt)



            // Define the area: x, y (bottom-left), width, height
            // NOTE: (0, 0) is top-left in Rectangle2D, but PDF coords are bottom-left!
            Rectangle2D.Float rect = new Rectangle2D.Float(100, 500, 200, 100);
            stripper.addRegion("targetArea", rect);


            // Register the region with the page
            stripper.extractRegions(page);

            // Now get the text from that area
            String text = stripper.getTextForRegion("targetArea");

            System.out.println("Text in area:");
            System.out.println(text);
        }

    }
}
