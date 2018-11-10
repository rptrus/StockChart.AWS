package com.rohan.stockapp.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.AbstractCategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.rohan.stockapp.dto.StockReportElement;
import com.rohan.stockapp.enums.ReportColors;
import com.rohan.stockapp.json.Status;
import com.rohan.stockapp.service.s3.S3Service;

@Component
public class ChartConstruction {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final int ALLOCATION_CHART =  0;
	private final int PERFORMANCE_CHART = 1;
	
	@Autowired
	private S3Service s3Service;
	
	@Value("${marginLeft:36}")	 
	Float marginLeft;
	@Value("${marginRight:36}")
	Float marginRight;
	@Value("${marginTop:54}")
	Float marginTop;
	@Value("${marginBottom:36}")
	Float marginBottom;
	
	@Value("${portFolioAllocationPagePositionX:38}")
	Integer portFolioAllocationPagePositionX;
	@Value("${portFolioAllocationPagePositionY:300}")
	Integer portFolioAllocationPagePositionY;	
	@Value("${performancePagePositionX:38}")
	Integer performancePagePositionX;
	@Value("${performancePagePositionY:38}")
	Integer performancePagePositionY;	
	
	@Value("${bgImage}")
	private String backgroundImageFile;
	
	@Value("${elementwidth:450}")
	private Integer elementWidth;	
	@Value("${elementheight:250}")
	private Integer elementHeight;
	
    Font bfBold12 = FontFactory.getFont("Verdana", 8, Font.BOLD);
    Font bfNormal = FontFactory.getFont("Verdana", 8, Font.NORMAL);
    Font bold = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    java.awt.Font smallVerdana = new java.awt.Font("Verdana", Font.NORMAL, 6);    
	
	public void makePDFChart(Status status, List<StockReportElement> stockElementList, String fullPathFilename, String bucketName, String email) {
		  PdfWriter writer = null;
		  Document document = new Document(PageSize.A4, marginLeft, marginRight, marginTop, marginBottom);
		  
	      try {
	    	  /*
	    	  File f = new File(fullPathFilename);
	    	  FileOutputStream fos = new FileOutputStream(f);
	    	  writer = PdfWriter.getInstance(document, fos);
	    	  */
	    	  System.out.println("*106*");
	    	  File f = new File(fullPathFilename);
	    	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	  writer = PdfWriter.getInstance(document, baos);
	    	  
	          document.open();	          
	          addTransparentBackgroundFromS3Bucket(writer);
	          addEmptyLine(document,  1);
              
	          Paragraph p = new Paragraph("Report Date:\t\t\t\t"+Utils.ddMMMyyyHHmm.format(Utils.getCurrentDate()), bold );
              p.setAlignment(Element.ALIGN_JUSTIFIED);
              document.add(p);
              addEmptyLine(document, 2);
              
              document.add(stockStatsTable(stockElementList));
              addEmptyLine(document,  1);
              
              addTemplateToPage(writer, stockElementList, ALLOCATION_CHART, elementWidth, elementHeight, portFolioAllocationPagePositionX, portFolioAllocationPagePositionY); 
              addTemplateToPage(writer, stockElementList, PERFORMANCE_CHART, elementWidth, elementHeight, performancePagePositionX, performancePagePositionY);

              addEmptyLine(document,  1);
              
              document.close();
              
              Random r = new Random(System.currentTimeMillis());
              String randomdigits = String.valueOf(r.nextInt(1000));
              String emailEncoded = StringUtils.removeEnd(Base64.getEncoder().encodeToString(email.getBytes()),"=");
              byte[] data = baos.toByteArray();
              long size = data.length;
              String key = String.format("%s_%s_%s", emailEncoded, randomdigits, f.getName());
              s3Service.uploadToS3(bucketName, key, new ByteArrayInputStream(data), size);
              status.setUrl("https://s3.us-east-1.amazonaws.com/"+bucketName+"/"+key); // hardcoded for now. We won't ever move off N.Virginia as it has SES
              logger.info("Document now closed.");
        } catch (Exception ex) {
        	logger.error("Exception! ",ex);
        	if (document!=null)
        		document.close();
        }
	}
	
	private void addTemplateToPage(PdfWriter writer, List<StockReportElement> stockElementList, int type, Integer width, Integer height, Integer pagePositionX, Integer pagePositionY) {		
		int stretchFactor0 = 0;
        PdfContentByte contentByte0 = writer.getDirectContent();
        PdfContentByte contentByteLine0 = writer.getDirectContent();
        PdfTemplate templateLine0 = contentByteLine0.createTemplate(elementWidth, elementHeight+stretchFactor0);
        Graphics2D graphics2dLine0 = templateLine0.createGraphics(elementWidth, elementHeight+stretchFactor0,
          		new DefaultFontMapper());
        Rectangle2D rectangle2dLine0 = new Rectangle2D.Double(0, 0, elementWidth, elementHeight+stretchFactor0);
        if (type == ALLOCATION_CHART) makePortfolioAllocationPieChart(stockElementList).draw(graphics2dLine0, rectangle2dLine0);
        if (type == PERFORMANCE_CHART) createPerformanceGraph(stockElementList).draw(graphics2dLine0, rectangle2dLine0);
        graphics2dLine0.dispose();
        contentByte0.addTemplate(templateLine0, pagePositionX, pagePositionY); // positioning on page
	}
	
	private void addTransparentBackgroundFromS3Bucket(PdfWriter writer) throws MalformedURLException, IOException, DocumentException {
        PdfContentByte canvas = writer.getDirectContentUnder();
        byte img[] = s3Service.readFromS3("noworriesmate", "ferny.JPG");
        Image bgimage = Image.getInstance(img);
        bgimage.scaleAbsoluteHeight(850);
        bgimage.setAbsolutePosition(0, 0);
        canvas.saveState();
        PdfGState state = new PdfGState();	             
        state.setFillOpacity(0.3f);
        canvas.setGState(state);
        canvas.addImage(bgimage);
        canvas.restoreState();		
	}

	
	private JFreeChart createPerformanceGraph(List<StockReportElement> stockList) {
        final CategoryDataset dataset1 = createDatasetPercentage(stockList);
        final NumberAxis rangeAxis1 = new NumberAxis("Performance");
        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        AbstractCategoryItemRenderer renderer1 = new BarRenderer();

        final CategoryPlot subplot1 = new CategoryPlot(dataset1, null, rangeAxis1, renderer1);
        subplot1.setDomainGridlinesVisible(true);
        ValueAxis yAxis =subplot1.getRangeAxis();
        ((BarRenderer) subplot1.getRenderer()).setBarPainter(new StandardBarPainter());
        ((BarRenderer) subplot1.getRenderer()).setMaximumBarWidth(0.085f);
        renderer1.setSeriesPaint(0, new Color(ReportColors.COOLBLUE.getReportColor()));
        
        CategoryItemLabelGenerator generator1 = new StandardCategoryItemLabelGenerator();
        renderer1.setSeriesItemLabelGenerator(0, generator1);
        renderer1.setSeriesItemLabelsVisible(0, true);
        renderer1.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(ItemLabelAnchor.CENTER,TextAnchor.BASELINE_CENTER));
        renderer1.setItemLabelAnchorOffset(10);

        
        final CategoryDataset dataset2 = createDatasetAmount(stockList);
        final NumberAxis rangeAxis2 = new NumberAxis("Holding Amt"); 
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        final BarRenderer renderer2 = new BarRenderer();        
        final CategoryPlot subplot2 = new CategoryPlot(dataset2, null, rangeAxis2, renderer2);
        subplot2.setDomainGridlinesVisible(true);
        ((BarRenderer) subplot2.getRenderer()).setBarPainter(new StandardBarPainter());
        ((BarRenderer) subplot2.getRenderer()).setMaximumBarWidth(0.085f);                

        final CategoryAxis domainAxis = new CategoryAxis("Stock Code");
        
        domainAxis.setTickLabelPaint(Color.BLACK);
        domainAxis.setTickLabelFont(smallVerdana);
        renderer2.setSeriesPaint(0, new Color(ReportColors.GREEN.getReportColor()));
        
        CategoryItemLabelGenerator generator2 = new StandardCategoryItemLabelGenerator();
        renderer2.setSeriesItemLabelGenerator(0, generator2);
        renderer2.setSeriesItemLabelsVisible(0, true);
        renderer2.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(ItemLabelAnchor.CENTER,TextAnchor.BASELINE_CENTER));
        renderer2.setItemLabelAnchorOffset(10);
        
        final CombinedDomainCategoryPlot plot = new  CombinedDomainCategoryPlot(domainAxis);

        plot.add(subplot1, 1);
        plot.add(subplot2, 1);        
        plot.setGap(18);

        final JFreeChart chart = new JFreeChart(
    		  "Performance Graph",
              new java.awt.Font("SansSerif", Font.BOLD, 12),
              plot,
              true
        );
        return chart;
	}
	  
	private JFreeChart makePortfolioAllocationPieChart(List<StockReportElement> stockList) {
		  DefaultPieDataset dataset = new DefaultPieDataset( );

		  BigDecimal accumulated = BigDecimal.ZERO;
		  for (StockReportElement stock : stockList) {			  
			  BigDecimal shareHolding = stock.getCurrentPrice().multiply(BigDecimal.valueOf(stock.getNumberOfUnits()));
			  accumulated = accumulated.add(shareHolding);
		  }
		  
		  for (StockReportElement stock : stockList) {
			  BigDecimal shareHolding = stock.getCurrentPrice().multiply(BigDecimal.valueOf(stock.getNumberOfUnits()));
			  BigDecimal portion = shareHolding.divide(accumulated, 2, RoundingMode.HALF_UP).multiply(BigDecimal.TEN.pow(2));
			  dataset.setValue(stock.getCode(), portion.doubleValue());
		  }


	      JFreeChart chart = ChartFactory.createPieChart(
	         "Share Portfolio Allocation",   // chart title
	         dataset,          // data
	         true,             // include legend
	         true,
	         false);
	        
	      chart.setBackgroundPaint(new ChartColor(224, 224, 224));
	      return chart;
	  }
	
    public CategoryDataset createDatasetPercentage(List<StockReportElement> stockList) {
          final DefaultCategoryDataset result = 
          new DefaultCategoryDataset();
          final String rowKey = "Increase %";
          
          for (StockReportElement aStockReportElement: stockList) {
        	  result.addValue(
        			  aStockReportElement.getCurrentPrice()
        					  	.divide(aStockReportElement.getAcquiredPrice(),2,RoundingMode.HALF_EVEN)
        						.subtract(BigDecimal.ONE)
        						.multiply(BigDecimal.TEN.pow(2)).setScale(0).doubleValue(), 
        			  rowKey, 
        			  aStockReportElement.getCode()
        			  );
          }
        return result;
    }
    
    public CategoryDataset createDatasetAmount(List<StockReportElement> stockList) {
          final DefaultCategoryDataset result = new DefaultCategoryDataset();
          final String rowKey = "Equity held";
          
          for (StockReportElement aStockReportElement: stockList) {
        	  result.addValue(
        			  aStockReportElement.getCurrentPrice().doubleValue()*aStockReportElement.getNumberOfUnits(), 
        			  rowKey, 
        			  aStockReportElement.getCode()
        			  );
          }
          return result;
    }

	  
	  private PdfPTable stockStatsTable(List<StockReportElement> stockList) throws ParseException
	    {
	        float[] columnWidths = {1.5f, 1.5f, 2f, 2f, 2f, 2f};
	           PdfPTable table = new PdfPTable(columnWidths);
	           table.setWidthPercentage(100f);
	           insertCell(table, "Code: ", Element.ALIGN_LEFT, 1, bfBold12, Boolean.TRUE);
	           insertCell(table, "Units: ", Element.ALIGN_RIGHT, 1, bfBold12, Boolean.TRUE);
	           insertCell(table, "Acquired Price", Element.ALIGN_RIGHT, 1, bfBold12, Boolean.TRUE);
	           insertCell(table, "Current Price", Element.ALIGN_RIGHT, 1, bfBold12, Boolean.TRUE);
	           insertCell(table, "Movement (price)", Element.ALIGN_RIGHT, 1, bfBold12, Boolean.TRUE);
	           insertCell(table, "Movement (percent)", Element.ALIGN_RIGHT, 1, bfBold12, Boolean.TRUE);
	           stockList.stream().filter(StockReportElement::active).forEach(stock->addStock(table, stock));
	           return table;
	    }
	  
	  private void addStock(PdfPTable table, StockReportElement stock) {
          insertCell(table, stock.getCode(), Element.ALIGN_LEFT, 1, bfNormal);
          insertCell(table, stock.getNumberOfUnits().toString(), Element.ALIGN_RIGHT, 1, bfNormal);
          insertCell(table, stock.getAcquiredPrice().setScale(2,RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT, 1, bfNormal);
          insertCell(table, stock.getCurrentPrice().setScale(2,RoundingMode.HALF_UP).toString(), Element.ALIGN_RIGHT, 1, bfNormal);
          insertCell(table, stock.getCurrentPrice().subtract(stock.getAcquiredPrice()).setScale(2, RoundingMode.HALF_EVEN)+"", Element.ALIGN_RIGHT, 1, bfNormal); // % by value 
          insertCell(table, (stock.getCurrentPrice().divide(stock.getAcquiredPrice(),4,RoundingMode.HALF_EVEN)
    				.subtract(BigDecimal.ONE))
    				.multiply(BigDecimal.TEN.pow(2)).setScale(2).toString()+"%", Element.ALIGN_RIGHT, 1, bfNormal); // % by number		  
	  }
	  
	  // HELPER FUNCTIONS
	  
	  private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {
		  insertCell(table, text, align, colspan, font, false);
	  }
	  
	  private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font, boolean header){
          PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
          cell.setHorizontalAlignment(align);
          cell.setColspan(colspan);
          //in case there is no text and you want to create an empty row
          if(text.trim().equalsIgnoreCase("")){
           cell.setMinimumHeight(10f);
          }
          cell.setBackgroundColor(new BaseColor(249, 255, 230,0));
          if (header) cell.setBackgroundColor(new BaseColor(237, 255, 179,0)); 
          table.addCell(cell);
           
         }
	
	  private static void addEmptyLine(Document document, int number) throws DocumentException {
		  Paragraph paragraph = new Paragraph();
	        for (int i = 0; i < number; i++) {
	          paragraph.add(new Paragraph(" "));
	          document.add(paragraph);
	        }
	    }
	  
}
