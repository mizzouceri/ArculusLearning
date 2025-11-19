package com.cyberrange.api.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.pdfbox.pdmodel.PDDocument; 
import org.apache.pdfbox.pdmodel.PDPage; 
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CertificateGenerator 
{
	@Value("${cyberrange.certificate-template-area}")
	private String templateLocation;
	
	@Value("${cyberrange.certificate-storage-area}")
	private String certificateLocation;
	
	public void manipulatePDF(Long moduleId, Long userId, String studentName)
	{
	  try 
	  {
		  File certificatePDF = new File(templateLocation.toString() + "/" + moduleId + "/MCDCertificate.pdf");
		  PDDocument newPdf = PDDocument.load(certificatePDF);
		  PDPage firstPage=newPdf.getPage(0);
		  PDFont pdfFont= PDType1Font.HELVETICA_BOLD;
		  float fontSize = 18;
		  PDPageContentStream contentStream = new PDPageContentStream(newPdf, firstPage, PDPageContentStream.AppendMode.APPEND, true, true);  
		  //PDPageContentStream contentStream = new PDPageContentStream(newPdf, firstPage, PDPageContentStream.AppendMode.APPEND,true,true);
		  contentStream.setFont(pdfFont, fontSize);
		  contentStream.beginText();
		  contentStream.newLineAtOffset(400, firstPage.getTrimBox().getHeight()-360);
		  contentStream.showText(studentName);
		  contentStream.endText();
		  contentStream.close();
		  newPdf.save(certificateLocation.toString() + "/" + moduleId + "/User_" + userId + "_Certificate.pdf") ;
		  newPdf.close();
		  System.out.println("Saved");
	  }	  
	  catch(Exception ex)
	  {
		  
	  }  
	}
	
	public InputStream downloadPDF(Long moduleId, Long userId)
	{
	  try 
	  {
		  InputStream inputStream = new FileInputStream(certificateLocation.toString() + "/" + moduleId + "/User_" + userId + "_Certificate.pdf");
		  return inputStream;
	  }	  
	  catch(Exception ex)
	  {
		  
	  }
	  return null;  
	}
	
//	public static void main(String[] a)
//	{
//		new CertificateGenerator().manipulatePDF((long ) 1,(long ) 1, "Hemanth Sai");
//	}
}

  

