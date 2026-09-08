package io.github.jowgrin.imposingpdf.PDFServices;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RotateDocument {
    private PDDocument document;
    private int seriesOf;
    private String command;        // עמדות 1-based בתוך הסדרה שיסתובבו, למשל "1,3"
    private int rotationAngle;      // זווית סיבוב קבועה במעלות

    public RotateDocument(PDDocument document) {
        this.document = document;
        this.seriesOf = 1;
        this.rotationAngle = 90;
    }

    public int getSeriesOf() { return seriesOf; }
    public void setSeriesOf(int seriesOf) {
        if (seriesOf < 1) return;
        this.seriesOf = seriesOf;
    }

    public String getCommand() { return command; }
    public void setCommand(String command) { this.command = command; }

    public int getRotationAngle() { return rotationAngle; }
    public void setRotationAngle(int rotationAngle) { this.rotationAngle = rotationAngle; }

    /**
     * עובר על המסמך בסדרות (chunks) בגודל seriesOf דפים.
     * סדר הדפים עצמו לא משתנה - רק זווית הסיבוב של דפים נבחרים.
     *
     * בחירת הדפים שיסתובבו בכל סדרה:
     *  - isShuffle=false: בדיוק העמדות שב-command (1-based, יכול להיות
     *    חלק מהסדרה בלבד, למשל "1,3" עבור סדרה של 4).
     *  - isShuffle=true: אותה כמות דפים כמו שיש איברים ב-command,
     *    אבל אילו עמדות בפועל - נבחר אקראית מחדש בכל סדרה.
     *
     * זווית הסיבוב לכל דף שנבחר:
     *  - isShuffleAngle=false: rotationAngle הקבוע.
     *  - isShuffleAngle=true: זווית אקראית מתוך {0,90,180,270} לכל דף בנפרד.
     */
    public PDDocument create() throws IOException {
        if (document == null || document.getNumberOfPages() == 0) {
            throw new IllegalArgumentException("Source document is null or empty");
        }
        if (command == null || command.trim().isEmpty()) {
            throw new IllegalArgumentException("command must be set (defines which/how many positions rotate)");
        }

        int[] fixedPositions = parseCommand(command, seriesOf); // 0-based, ללא כפילויות, בטווח
        int selectionCount = fixedPositions.length;

        PDDocument result = new PDDocument();
        int pageCount = document.getNumberOfPages();
        Random random = new Random();

        for (int seriesStart = 0; seriesStart < pageCount; seriesStart += seriesOf) {
            int seriesLength = Math.min(seriesOf, pageCount - seriesStart);

            // אילו עמדות (0-based, בתוך הסדרה) יסתובבו בסדרה הנוכחית
            List<Integer> positionsToRotate = resolvePositionsToRotate(
                    fixedPositions, selectionCount, seriesLength, random);

            // עוברים על כל הדפים בסדרה, בסדר המקורי, ומחילים סיבוב רק על הנבחרים
            for (int localIndex = 0; localIndex < seriesLength; localIndex++) {
                int sourcePageIndex = seriesStart + localIndex;
                PDPage sourcePage = document.getPage(sourcePageIndex);
                PDPage newPage = result.importPage(sourcePage);

                if (positionsToRotate.contains(localIndex)) {
                	int addedRotation = rotationAngle;
                    int currentRotation = newPage.getRotation();
                    int finalRotation = normalizeRotation(currentRotation + addedRotation);
                    newPage.setRotation(finalRotation);
                }
                // דפים שלא נבחרו - נשארים ללא שינוי (rotation מקורי כמו שהוא)
            }
        }

        return result;
    }

    /**
     * קובע אילו עמדות 0-based (מוגבל ל-actualSeriesLength) יסתובבו בסדרה נתונה:
     *  - ללא shuffle: מחזיר את fixedPositions כמו שהם, מסונן לגודל הסדרה בפועל
     *    (רלוונטי רק לסדרה חלקית, בד"כ האחרונה).
     *  - עם shuffle: מגריל selectionCount עמדות שונות מתוך 0..actualSeriesLength-1.
     */
    private List<Integer> resolvePositionsToRotate(int[] fixedPositions, int selectionCount,
                                                     int actualSeriesLength, Random random) {
    	List<Integer> positions = new ArrayList<>();
        for (int pos : fixedPositions) {
            if (pos < actualSeriesLength) {
                positions.add(pos);
            }
        }
        return positions;
    }

    /**
     * מפרש command כמו "1,3" לעמדות 0-based: {0,2}.
     * מאפשר פחות איברים מ-seriesOf (בחירה חלקית), אבל בלי כפילויות ובלי חריגה מהטווח.
     */
    private int[] parseCommand(String command, int maxSize) {
        String[] parts = command.split(",");
        if (parts.length == 0) {
            throw new IllegalArgumentException("command must contain at least one index");
        }
        if (parts.length > maxSize) {
            throw new IllegalArgumentException(
                    "command contains more indices (" + parts.length + ") than seriesOf (" + maxSize + ")");
        }

        int[] result = new int[parts.length];
        boolean[] seen = new boolean[maxSize + 1];

        for (int i = 0; i < parts.length; i++) {
            int value;
            try {
                value = Integer.parseInt(parts[i].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in command: '" + parts[i] + "'");
            }
            if (value < 1 || value > maxSize) {
                throw new IllegalArgumentException(
                        "command index " + value + " out of range 1.." + maxSize);
            }
            if (seen[value]) {
                throw new IllegalArgumentException("command contains duplicate index: " + value);
            }
            seen[value] = true;
            result[i] = value - 1; // 0-based
        }

        return result;
    }

    /** מנרמל זווית סיבוב לטווח 0-359 מעלות. */
    private int normalizeRotation(int degrees) {
        int normalized = degrees % 360;
        if (normalized < 0) {
            normalized += 360;
        }
        return normalized;
    }
}