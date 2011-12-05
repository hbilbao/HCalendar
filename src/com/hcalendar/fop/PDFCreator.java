package com.hcalendar.fop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.hcalendar.config.ConfigurationNotInitedException;
import com.hcalendar.config.ConfigurationUtils;
import com.hcalendar.data.dto.WorkInputsDTO;

/**
 * Create pdf of WorkInputsDTO class using JAXP (XSLT) and FOP (XSL:FO).
 */
public class PDFCreator {

	// configure fopFactory as desired
	private FopFactory fopFactory = FopFactory.newInstance();

	/**
	 * Converts a WorkInputsDTO object to a PDF file.
	 * 
	 * @param workInput
	 *            the WorkInputsDTO object
	 * @param xslt
	 *            the stylesheet file
	 * @param pdf
	 *            the target PDF file
	 * @throws IOException
	 *             In case of an I/O problem
	 * @throws FOPException
	 *             In case of a FOP problem
	 * @throws TransformerException
	 *             In case of a XSL transformation problem
	 * @throws URISyntaxException
	 *             No stylesheet found
	 * @throws ConfigurationNotInitedException
	 */
	public void createPDF(WorkInputsDTO workInput) throws IOException,
			FOPException, TransformerException, ConfigurationNotInitedException {

		InputStream xsltfileInputStream = ConfigurationUtils.getPDFStyleSheet();
		File pdffile = ConfigurationUtils.getPDFTempFile();

		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		// configure foUserAgent as desired

		// Setup output
		OutputStream out = new java.io.FileOutputStream(pdffile);
		out = new java.io.BufferedOutputStream(out);
		try {
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(org.apache.xmlgraphics.util.MimeConstants.MIME_PDF, foUserAgent,
					out);

			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					xsltfileInputStream));

			// Setup input for XSLT transformation
			Source src = workInput.getSource();

			// Resulting SAX events (the generated FO) must be piped through to
			// FOP
			Result res = new SAXResult(fop.getDefaultHandler());

			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} finally {
			out.close();
		}
	}
}
