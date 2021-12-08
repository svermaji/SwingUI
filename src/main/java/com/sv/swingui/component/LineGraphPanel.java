package com.sv.swingui.component;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class LineGraphPanel extends AppPanel {

    private final List<LineGraphPanelData> data;
    // size of point
    private int pointWidth = 8;
    private int lineWidth = 2;
    private Color pointColor = Color.red, lineColor = Color.green, fontColor = Color.blue;
    private int margin = 50;
    private Point mousePoint;
    private Font graphFont;

    public LineGraphPanel(List<LineGraphPanelData> data) {
        this.data = data;

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

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setPointWidth(int pointWidth) {
        this.pointWidth = pointWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D) g;
        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g1.setStroke(new BasicStroke(lineWidth));
        g1.draw(new Line2D.Double(margin, margin, margin, height - margin));
        g1.draw(new Line2D.Double(margin, height - margin, width - margin, height - margin));

        double x = (double) (width - 2 * margin) / (data.size() - 1);
        double scale = (double) (height - 2 * margin) / getMaxOfValues();
        for (int i = 0; i < data.size(); i++) {
            LineGraphPanelData graphPoint = data.get(i);
            int val = graphPoint.getValue();
            double x1 = margin + i * x;
            double y1 = height - margin - scale * val;
            int l2x = (int) x1, l2y = (int) y1;
            setFont(graphFont);
            g1.setFont(graphFont);
            g1.setPaint(fontColor);
            g1.setStroke(new BasicStroke(pointWidth));
            g1.drawString(graphPoint.getNameToDisplay(), l2x - pointWidth * 2, l2y - pointWidth);
            g1.setPaint(pointColor);
            Ellipse2D.Double point = new Ellipse2D.Double(x1 - pointWidth, y1 - pointWidth, pointWidth * 2, pointWidth * 2);
            g1.fill(point);
            if (mousePoint != null && point.contains(mousePoint)) {
                setToolTipText(graphPoint.getFullNameToDisplay());
            }
            // to join lines it must be from 2nd point
            if (i > 0) {
                double x0 = margin + (i - 1) * x;
                double y0 = height - margin - scale * data.get(i - 1).getValue();
                g1.setPaint(lineColor);
                g1.setStroke(new BasicStroke(lineWidth));
                int l1x = (int) x0, l1y = (int) y0;
                g1.drawLine(l1x, l1y, l2x, l2y);
            }
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
        frame.add(new LineGraphPanel(data));
        frame.setSize(400, 400);
        frame.setLocation(200, 200);
        frame.setVisible(true);
    }
}
