package com.sv.swingui.component;

import com.sv.core.logger.MyLogger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.StringJoiner;

public class LineGraphPanel extends AppPanel {

    private final MyLogger logger;
    private List<LineGraphPanelData> data;
    private LineGraphPanelData data00 = new LineGraphPanelData(0);
    private List<String> toPrint;
    // size of point
    private int pointWidth = 8;
    private int lineWidth = 2;
    // used to draw lines in better way
    private int yAxisGap = 100;
    private int margin = 50;
    private boolean lineJoinsPointsCenter = false, drawBaseLines = true, firstPointOnBaseLine = false;
    private Color pointColor = Color.red, lineColor = Color.green, fontColor = Color.blue;
    private Point mousePoint;
    private Font graphFont;
    private Stroke lineStroke, pointStroke;

    public LineGraphPanel() {
        this(null);
    }

    public LineGraphPanel(MyLogger logger) {
        this(null, logger);
    }

    public LineGraphPanel(List<LineGraphPanelData> data, MyLogger logger) {
        this.logger = logger;
        setData((data == null) ? new ArrayList<>() : data);

        // to init strokes
        setLineJoinsPointsCenter(lineJoinsPointsCenter);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePoint = e.getPoint();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        });
    }

    public void setData(List<LineGraphPanelData> data) {
        this.data = data;
        toPrint = new ArrayList<>();
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setPointWidth(int pointWidth) {
        this.pointWidth = pointWidth;
        setLineJoinsPointsCenter(lineJoinsPointsCenter);
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        setLineJoinsPointsCenter(lineJoinsPointsCenter);
    }

    public void setPointColor(Color pointColor) {
        this.pointColor = pointColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public void setGraphFont(Font font) {
        this.graphFont = font;
    }

    public void setyAxisGap(int yAxisGap) {
        this.yAxisGap = yAxisGap;
    }

    // will be called by reflection
    public void setLineJoinsPointsCenter(Boolean lineJoinsPointsCenter) {
        this.lineJoinsPointsCenter = lineJoinsPointsCenter;
        lineStroke = new BasicStroke(lineWidth, lineJoinsPointsCenter ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND);
        pointStroke = new BasicStroke(pointWidth, lineJoinsPointsCenter ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND);

    }

    // will be called by reflection
    public void setDrawBaseLines(Boolean drawBaseLines) {
        this.drawBaseLines = drawBaseLines;
    }

    // will be called by reflection
    public void setFirstPointOnBaseLine(Boolean firstPointOnBaseLine) {
        this.firstPointOnBaseLine = firstPointOnBaseLine;
    }

    public boolean isLineJoinsPointsCenter() {
        return lineJoinsPointsCenter;
    }

    public boolean isDrawBaseLines() {
        return drawBaseLines;
    }

    public boolean isFirstPointOnBaseLine() {
        return firstPointOnBaseLine;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (drawBaseLines) {
            g1.setStroke(lineStroke);
            g1.draw(new Line2D.Double(margin, margin, margin, height - margin));
            g1.draw(new Line2D.Double(margin, height - margin, width - margin, height - margin));
        }

        if (data.size() == 1) {
            data.add(0, data00);
        }
        int dataSize = data.size();
        double x = (double) (width - 2 * margin) / data.size();
        if (firstPointOnBaseLine) {
            x = (double) (width - 2 * margin) / (data.size() - 1);
        }
        double scale = (double) (height - 2 * margin) / getMaxOfValues();
        for (int i = 0; i < dataSize; i++) {
            LineGraphPanelData graphPoint = data.get(i);
            int val = graphPoint.getValue();
            double x1 = margin + (i + 1) * x;
            if (firstPointOnBaseLine) {
                x1 = margin + i * x;
            }
            if (dataSize == 1 && firstPointOnBaseLine) {
                x1 = x1 + (margin / 2);
            }
            double y1 = height - margin - scale * val;
            int l2x = (int) x1, l2y = (int) y1;

            if (toPrint != null) {
                toPrint.add("Point/value [" + (i + 1) + "/" + val + "] x,y are [" + l2x + "," + l2y + "]");
            }
            setFont(graphFont);
            g1.setFont(graphFont);
            g1.setPaint(fontColor);
            g1.setStroke(pointStroke);
            g1.drawString(graphPoint.getNameToDisplay(), l2x - pointWidth * 2, l2y - pointWidth);
            g1.setPaint(pointColor);
            Ellipse2D.Double point = new Ellipse2D.Double(x1 - pointWidth, y1 - pointWidth, pointWidth * 2, pointWidth * 2);
            g1.fill(point);
            if (mousePoint != null && point.contains(mousePoint)) {
                setToolTipText(graphPoint.getFullNameToDisplay());
            }
            // to join lines it must be from 2nd point
            if (i > 0) {
                double x0 = margin + i * x;
                if (firstPointOnBaseLine) {
                    x0 = margin + (i - 1) * x;
                }
                double y0 = height - margin - scale * data.get(i - 1).getValue();
                g1.setPaint(lineColor);
                g1.setStroke(lineStroke);
                int l1x = (int) x0, l1y = (int) y0;

                if (!lineJoinsPointsCenter) {
                    // check if next point is up or down
                    int l1Diff = l1y - l2y;
                    int l2Diff = l1Diff * -1;
                    int l1xOrig = l1x;
                    int l2xOrig = l2x;
                    int l1yOrig = l1y;
                    int l2yOrig = l2y;
                    int fraction = pointWidth;
                    if (l1Diff > 0 && l1Diff > yAxisGap) {
                        //l1x = l1x + fraction;
                        l1y = l1y - fraction;
                        //l2x = l2x - fraction;
                        l2y = l2y + fraction;
                    } else if (l2Diff > 0 && l2Diff > yAxisGap) {
                        //l1x = l1x + fraction;
                        l1y = l1y + fraction;
                        //l2x = l2x - fraction;
                        l2y = l2y - fraction;
                    } else {
                        l1x = l1x + fraction;
                        l2x = l2x - fraction;
                    }
                    if (i == 1) {
                        if (l1Diff > 0 && l1Diff > yAxisGap) {
                            //l1x = l1xOrig;
                            l1y = l1yOrig - pointWidth;
                        } else if (l2Diff > 0 && l2Diff > yAxisGap) {
                            //l1x = l1xOrig;
                            l1y = l1yOrig + pointWidth;
                        }
                    }
                    if (i == dataSize - 1) {
                        if (l1Diff > 0 && l1Diff > yAxisGap) {
                            //l2x = l2xOrig;
                            l2y = l2yOrig + pointWidth;
                        } else if (l2Diff > 0 && l2Diff > yAxisGap) {
                            //l2x = l2xOrig;
                            l2y = l2yOrig - pointWidth;
                        }
                    }
                }
                // need to check for 1st and last element to connect with line
                g1.drawLine(l1x, l1y, l2x, l2y);
            }
        }
        // print only once
        if (logger != null && toPrint != null) {
            StringBuilder sb = new StringBuilder("Line graph points: ");
            StringJoiner joiner = new StringJoiner(", ");
            toPrint.forEach(joiner::add);
            logger.info(sb.toString() + joiner);
            toPrint = null;
        }
    }

    private int getMaxOfValues() {
        int max = -Integer.MAX_VALUE;
        OptionalInt maxList = data.stream().mapToInt(LineGraphPanelData::getValue).max();
        return maxList.isPresent() && maxList.getAsInt() > max ? maxList.getAsInt() : max;
    }

    public static void main(String[] args) {
        AppFrame frame = new AppFrame("Test");
        frame.setDefaultCloseOperation(AppFrame.EXIT_ON_CLOSE);
        List<LineGraphPanelData> data = new ArrayList<>();
        data.add(new LineGraphPanelData(10000, "Test"));
        data.add(new LineGraphPanelData(2000, "Test"));
        data.add(new LineGraphPanelData(20000, "Test"));
        data.add(new LineGraphPanelData(30000, "Test"));
        data.add(new LineGraphPanelData(0, "Test"));
        data.add(new LineGraphPanelData(15000, "Test"));
        frame.add(new LineGraphPanel(data, null));
        frame.setSize(400, 400);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }

    @Override
    public String toString() {
        return "LineGraphPanel{" +
                "pointWidth=" + pointWidth +
                ", lineWidth=" + lineWidth +
                ", yAxisGap=" + yAxisGap +
                ", margin=" + margin +
                ", linesJoinPoint=" + lineJoinsPointsCenter +
                ", drawBaseLines=" + drawBaseLines +
                ", firstPointOnBaseLine=" + firstPointOnBaseLine +
                '}';
    }
}
