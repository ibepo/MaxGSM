package com.bepo.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XChart;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.info.AnchorDataPoint;
import org.xclcharts.renderer.plot.PlotGrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.bepo.core.DemoView;

public class SplineChart01View extends DemoView {

	private String TAG = "SplineChart01View";
	private SplineChart chart = new SplineChart();
	// �������ǩ����
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<SplineData> chartData = new LinkedList<SplineData>();
	Paint pToolTip = new Paint(Paint.ANTI_ALIAS_FLAG);

	public SplineChart01View(Context context) {
		super(context);
		initView();
	}

	public SplineChart01View(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SplineChart01View(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		chartLabels();
		chartDataSet();
		chartRender();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// ͼ��ռ��Χ��С
		chart.setChartRange((float) (w), h);
	}

	private void chartRender() {
		try {

			chart.getPlotLegend().hide();
			chart.disableScale();// ��ֹ����
			chart.disablePanMode();// ��ֹ����
			// ���û�ͼ��Ĭ������pxֵ,���ÿռ���ʾAxis,Axistitle....
			int[] ltrb = getBarLnDefaultSpadding();
			chart.setPadding(ltrb[0], 50, ltrb[2], 60);

			// ƽ��ʱ������
			// float margin = DensityUtil.dip2px(getContext(), 20);
			// chart.setXTickMarksOffsetMargin(margin);

			// ��ʾ�߿�
			// chart.showRoundBorder();

			// ����Դ
			chart.setCategories(labels);
			chart.setDataSource(chartData);

			// �����
			// chart.getAxisTitle().setLeftTitle("�ύ����");
			// chart.getAxisTitle().setLowerTitle("ʱ��");

			// ����ϵ
			chart.getDataAxis().setAxisMax(5);// ���������ֵ
			chart.getDataAxis().setAxisMin(0);// ��������Сֵ
			chart.getDataAxis().setAxisSteps(1);// ������̶ȼ��
			chart.getDataAxis().hideAxisLine();// ������
			chart.getDataAxis().hideTickMarks();// ���ر�ע��
			// chart.getDataAxis().hideAxisLabels();// ���ر�ǩ

			chart.setCategoryAxisMax(7);// ��ǩ�����ֵ
			chart.setCategoryAxisMin(1);// ��ǩ����Сֵ
			chart.getCategoryAxis().setAxisSteps(1);
			// chart.getCategoryAxis().hideAxisLine();
			chart.getCategoryAxis().hideTickMarks();
			// chart.getCategoryAxis().hideAxisLabels();

			// ����ͼ�ı���ɫ
			// chart.setBackgroupColor(true,Color.BLACK);
			// ���û�ͼ���ı���ɫ
			// chart.getPlotArea().setBackgroupColor(true, Color.WHITE);

			// ��������
			PlotGrid plot = chart.getPlotGrid();
			plot.showHorizontalLines();
			// plot.showVerticalLines();
			plot.getHorizontalLinePaint().setStrokeWidth(1);
			plot.getHorizontalLinePaint().setColor(Color.rgb(127, 204, 204));
			plot.setHorizontalLineStyle(XEnum.LineStyle.SOLID);

			// ��������ɺͺ���������һ���ʹ�С����ɫ,��ʾ�¶����ԣ�����ʵ��˽϶�
			// chart.getDataAxis().getAxisPaint().setStrokeWidth(plot.getHorizontalLinePaint().getStrokeWidth());
			chart.getCategoryAxis().getAxisPaint().setStrokeWidth(plot.getHorizontalLinePaint().getStrokeWidth());

			// chart.getDataAxis().getAxisPaint().setColor(Color.rgb(127, 204,
			// 204));
			chart.getCategoryAxis().getAxisPaint().setColor(Color.rgb(127, 204, 204));

			// chart.getDataAxis().getTickMarksPaint().setColor(Color.rgb(127,
			// 204, 204));
			chart.getCategoryAxis().getTickMarksPaint().setColor(Color.rgb(127, 204, 204));

			chart.getDataAxis().setHorizontalTickAlign(Align.LEFT);
			chart.getDataAxis().getTickLabelPaint().setTextAlign(Align.LEFT);
			// chart.getDataAxis().getAxisPaint().setTextSize(350);
			chart.getDataAxis().setTickLabelMargin(50);// ��ǩ��y��ľ���
			chart.getCategoryAxis().setTickLabelMargin(30);// ��ǩ��x��ľ���

			// ������ʾ��
			// plot.hideHorizontalLines();
			// plot.hideVerticalLines();
			// chart.setDataAxisLocation(XEnum.AxisLocation.VERTICAL_CENTER);
			// chart.setCategoryAxisLocation(XEnum.AxisLocation.HORIZONTAL_CENTER);

			// ���彻����ǩ��ʾ��ʽ,�ر�ע,������ͼ�������ԣ����Է��ظ�ʽΪ: xֵ,yֵ
			// �����з�������
			chart.setDotLabelFormatter(new IFormatterTextCallBack() {

				@Override
				public String textFormatter(String value) {
					String label = "(" + value + ")";
					return (label);
				}

			});
			// ����
			// chart.setTitle("��7��ͳ����(����/����)");
			// chart.addSubtitle("(XCL-Charts Demo)");

			// ����������
			// chart.ActiveListenItemClick();
			// Ϊ���ô�������������������5px�ĵ��������Χ
			chart.extPointClickRange(5);
			chart.showClikedFocus();

			// ��ʾʮ�ֽ�����
			// chart.showDyLine();
			chart.getDyLine().setDyLineStyle(XEnum.DyLineStyle.Vertical);
			// ����ʵ�ʻ��ƿ��
			// chart.getPlotArea().extWidth(500.f);

			// �����
			// chart.setAxesClosed(true);

			// ������ʾΪֱ�ߣ�������ƽ����
			chart.setCrurveLineStyle(XEnum.CrurveLineStyle.BEELINE);

			// ��ʹ�þ�ȷ���㣬����Java�������,�������
			chart.disableHighPrecision();

			// ��ע
			List<AnchorDataPoint> mAnchorSet = new ArrayList<AnchorDataPoint>();

			AnchorDataPoint an1 = new AnchorDataPoint(2, 0, XEnum.AnchorStyle.CIRCLE);
			an1.setAlpha(200);
			an1.setBgColor(Color.RED);
			an1.setAreaStyle(XEnum.DataAreaStyle.FILL);

			AnchorDataPoint an2 = new AnchorDataPoint(1, 1, XEnum.AnchorStyle.CIRCLE);
			an2.setBgColor(Color.GRAY);

			AnchorDataPoint an3 = new AnchorDataPoint(0, 2, XEnum.AnchorStyle.RECT);
			an3.setBgColor(Color.BLUE);

			mAnchorSet.add(an1);
			mAnchorSet.add(an2);
			mAnchorSet.add(an3);
			// chart.setAnchorDataPoint(mAnchorSet);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.toString());
		}
	}

	private void chartDataSet() {
		/*
		 * //��1�����ݼ� List<PointD> linePoint1 = new ArrayList<PointD>();
		 * linePoint1.add(new PointD(5d, 8d));
		 * 
		 * linePoint1.add(new PointD(12d, 12d)); linePoint1.add(new PointD(25d,
		 * 15d)); linePoint1.add(new PointD(30d, 30d)); linePoint1.add(new
		 * PointD(45d, 25d));
		 * 
		 * linePoint1.add(new PointD(55d, 33d)); linePoint1.add(new PointD(62d,
		 * 45d)); SplineData dataSeries1 = new SplineData("����ܲ�����",linePoint1,
		 * Color.rgb(54, 141, 238) ); //����Ūϸ��
		 * dataSeries1.getLinePaint().setStrokeWidth(2);
		 * 
		 * //��2�����ݼ� List<PointD> linePoint2 = new ArrayList<PointD>();
		 * linePoint2.add(new PointD(40d, 50d)); linePoint2.add(new PointD(55d,
		 * 55d)); linePoint2.add(new PointD(60d, 65d)); linePoint2.add(new
		 * PointD(65d, 85d));
		 * 
		 * linePoint2.add(new PointD(72d, 70d)); linePoint2.add(new PointD(85d,
		 * 68d)); SplineData dataSeries2 = new SplineData("���ܹ�",linePoint2,
		 * Color.rgb(255, 165, 132) );
		 * 
		 * 
		 * dataSeries2.setLabelVisible(true);
		 * dataSeries2.setDotStyle(XEnum.DotStyle.RECT);
		 * dataSeries2.getDotLabelPaint().setColor(Color.RED);
		 * 
		 * //�趨����Դ chartData.add(dataSeries1); chartData.add(dataSeries2);
		 */

		// ��1�����ݼ�
		List<PointD> linePoint1 = new ArrayList<PointD>();

		linePoint1.add(new PointD(1, 0));
		linePoint1.add(new PointD(2, 1));
		linePoint1.add(new PointD(3, 2));
		linePoint1.add(new PointD(4, 1));
		linePoint1.add(new PointD(5, 5));
		linePoint1.add(new PointD(6, 3));
		linePoint1.add(new PointD(7, 4));

		SplineData dataSeries1 = new SplineData("��7��ͳ����(����/����)", linePoint1, Color.rgb(255, 165, 132));

		dataSeries1.getLinePaint().setStrokeWidth(5); // ����Ūϸ��
		dataSeries1.setDotStyle(XEnum.DotStyle.RING);

		// ��2�����ݼ�
		// List<PointD> linePoint2 = new ArrayList<PointD>();
		// linePoint2.add(new PointD(1d, 50d));
		// linePoint2.add(new PointD(2d, 52d));
		// linePoint2.add(new PointD(3d, 53d));
		// linePoint2.add(new PointD(8d, 55d));
		// SplineData dataSeries2 = new SplineData("���ܹ�", linePoint2,
		// Color.rgb(255, 165, 132));
		//
		// dataSeries2.setLabelVisible(true);
		// dataSeries2.setDotStyle(XEnum.DotStyle.RECT);
		// dataSeries2.getDotLabelPaint().setColor(Color.RED);
		//
		// // ����round���ı�ǩ
		// // dataSeries2.getLabelOptions().showBackground();
		// dataSeries2.getLabelOptions().getBox().getBackgroundPaint().setColor(Color.GREEN);
		// dataSeries2.getLabelOptions().getBox().setRoundRadius(8);
		// dataSeries2.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.RECT);

		chartData.add(dataSeries1);
		// chartData.add(dataSeries2);
	}

	private void chartLabels() {
		labels.add("");
		labels.add("2");
		labels.add("3");
		labels.add("4");
		labels.add("5");
		labels.add("6");
		labels.add("7");

	}

	@Override
	public void render(Canvas canvas) {
		try {
			chart.render(canvas);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public List<XChart> bindChart() {
		List<XChart> lst = new ArrayList<XChart>();
		lst.add(chart);
		return lst;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			triggerClick(event.getX(), event.getY());
		}
		return true;
	}

	// ��������
	private void triggerClick(float x, float y) {
		// ������
		if (chart.getDyLineVisible())
			chart.getDyLine().setCurrentXY(x, y);
		if (!chart.getListenItemClickStatus()) {
			if (chart.getDyLineVisible() && chart.getDyLine().isInvalidate())
				this.invalidate();
		} else {
			PointPosition record = chart.getPositionRecord(x, y);
			if (null == record)
				return;

			if (record.getDataID() >= chartData.size())
				return;
			SplineData lData = chartData.get(record.getDataID());
			List<PointD> linePoint = lData.getLineDataSet();
			int pos = record.getDataChildID();
			int i = 0;
			Iterator it = linePoint.iterator();
			while (it.hasNext()) {
				PointD entry = (PointD) it.next();

				if (pos == i) {
					Double xValue = entry.x;
					Double yValue = entry.y;

					float r = record.getRadius();
					chart.showFocusPointF(record.getPosition(), r * 2);
					chart.getFocusPaint().setStyle(Style.STROKE);
					chart.getFocusPaint().setStrokeWidth(3);
					if (record.getDataID() >= 2) {
						chart.getFocusPaint().setColor(Color.BLUE);
					} else {
						chart.getFocusPaint().setColor(Color.RED);
					}

					// �ڵ������ʾtooltip
					pToolTip.setColor(Color.RED);
					chart.getToolTip().setCurrentXY(x, y);
					chart.getToolTip().addToolTip(" Key:" + lData.getLineKey(), pToolTip);
					chart.getToolTip().addToolTip(" Label:" + lData.getLabel(), pToolTip);
					chart.getToolTip().addToolTip(
							" Current Value:" + Double.toString(xValue) + "," + Double.toString(yValue), pToolTip);
					chart.getToolTip().getBackgroundPaint().setAlpha(100);
					this.invalidate();

					break;
				}
				i++;
			}// end while
		}
	}

}
