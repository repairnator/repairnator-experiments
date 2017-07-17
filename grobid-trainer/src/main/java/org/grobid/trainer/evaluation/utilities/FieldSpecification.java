package org.grobid.trainer.evaluation.utilities;

import java.util.*;

/**
 * Specification of field XML paths in different result documents for evaluation.
 *
 * @author Patrice Lopez
 */
public class FieldSpecification {

	public String fieldName = null;
	
	public List<String> nlmPath = new ArrayList<String>();
	public List<String> grobidPath = new ArrayList<String>();
	public List<String> pdfxPath = new ArrayList<String>();
	public List<String> cerminePath = new ArrayList<String>();
	
	public boolean isTextual = false;
	
	/** 
	 *  This static method instanciates the fields with the appropriate paths
	 *  in the different gold and extraction formats. 
	*/  
	public static void setUpFields(List<FieldSpecification> headerFields,
								List<FieldSpecification> fulltextFields,
								List<FieldSpecification> citationsFields, 
								List<String> headerLabels,
								List<String> fulltextLabels,
								List<String> citationsLabels) {
		// header

		// title
		FieldSpecification titleField = new FieldSpecification();
		titleField.fieldName = "title";
		titleField.isTextual = true;
		titleField.grobidPath.add("//titleStmt/title/text()");
		titleField.nlmPath.add("/article/front/article-meta/title-group/article-title//text()");
		titleField.pdfxPath.add("/pdfx/article/front/title-group/article-title/text()");
		headerFields.add(titleField);
		headerLabels.add("title");

		// authors
		FieldSpecification authorField = new FieldSpecification();
		authorField.fieldName = "authors";
		authorField.isTextual = true;
		//authorField.hasMultipleValue = true;
		/*authorField.grobidPath.
			add("//sourceDesc/biblStruct/analytic/author/persName/forename[@type=\"first\"]");
		authorField.grobidPath.
			add("//sourceDesc/biblStruct/analytic/author/persName/forename[@type=\"middle\"]");*/
		authorField.grobidPath.
			add("//sourceDesc/biblStruct/analytic/author/persName/surname/text()");
		//authorField.nlmPath.
		//	add("/article/front/article-meta/contrib-group/contrib[@contrib-type=\"author\"]/name/given-names");
		authorField.nlmPath.
			add("/article/front/article-meta/contrib-group/contrib[@contrib-type=\"author\"]/name/surname/text()");	
		authorField.pdfxPath.add("/pdfx/article/front/contrib-group/contrib[@contrib-type=\"author\"]/name/text()");
		headerFields.add(authorField);
		headerLabels.add("authors");

		// authors
		FieldSpecification firstAuthorField = new FieldSpecification();
		firstAuthorField.fieldName = "first_author";
		firstAuthorField.isTextual = true;
		/*firstAuthorField.grobidPath
			.add("//sourceDesc/biblStruct/analytic/author/persName/forename[@type=\"first\"]");
		firstAuthorField.grobidPath
			.add("//sourceDesc/biblStruct/analytic/author/persName/forename[@type=\"middle\"]");*/
		firstAuthorField.grobidPath
			.add("//sourceDesc/biblStruct/analytic/author[1]/persName/surname/text()");
		//firstAuthorField.nlmPath
		//	.add("/article/front/article-meta/contrib-group/contrib[@contrib-type=\"author\"]/name/given-names");
		firstAuthorField.nlmPath
			.add("/article/front/article-meta/contrib-group/contrib[@contrib-type=\"author\"][1]/name/surname/text()");	
		firstAuthorField.pdfxPath
			.add("/pdfx/article/front/contrib-group/contrib[@contrib-type=\"author\"][1]/name/text()");
		headerFields.add(firstAuthorField);
		headerLabels.add("first_author");

		// affiliation
		FieldSpecification affiliationField = new FieldSpecification();
		affiliationField.fieldName = "affiliations";
		affiliationField.isTextual = true;
		//affiliationField.hasMultipleValue = true;
		affiliationField.grobidPath.
			add("//sourceDesc/biblStruct/analytic/author/affiliation/orgName/text()");
		affiliationField.nlmPath.
			add("/article/front/article-meta/contrib-group/aff/text()");
		affiliationField.pdfxPath.add("/pdfx/article/front/contrib-group");
		//headerFields.add(affiliationField);
		//headerLabels.add("affiliations");

		// date
		FieldSpecification dateField = new FieldSpecification();
		dateField.fieldName = "date";
		dateField.grobidPath.
			add("//publicationStmt/date[1]/@when");
		dateField.nlmPath.
			add("/article/front/article-meta/pub-date[@pub-type=\"pmc-release\"][1]//text()");
		//headerFields.add(dateField);
		//headerLabels.add("date");

		// abstract
		FieldSpecification abstractField = new FieldSpecification();
		abstractField.fieldName = "abstract";
		abstractField.isTextual = true;
		abstractField.grobidPath.
			add("//profileDesc/abstract//text()");
		abstractField.nlmPath.
			add("/article/front/article-meta/abstract//text()");
		headerFields.add(abstractField);
		headerLabels.add("abstract");

		// keywords
		FieldSpecification keywordsField = new FieldSpecification();
		keywordsField.fieldName = "keywords";
		keywordsField.isTextual = true;
		keywordsField.grobidPath.
			add("//profileDesc/textClass/keywords//text()");
		keywordsField.nlmPath.
			add("/article/front/article-meta/kwd-group/kwd/text()");
		headerFields.add(keywordsField);
		headerLabels.add("keywords");

		// DOI
		FieldSpecification doiField = new FieldSpecification();
		doiField.fieldName = "doi";
		doiField.grobidPath.
			add("//sourceDesc/biblStruct/idno[@type=\"DOI\"]/text()");
		doiField.nlmPath.
			add("/article/front/article-meta/article-id[@pub-id-type=\"doi\"]/text()");
		//headerFields.add(doiField);
		//headerLabels.add("doi");

		// citations

		// the first field gives the base path for each citation structure
		FieldSpecification baseCitation = new FieldSpecification();
		baseCitation.fieldName = "base";
		baseCitation.grobidPath.
			add("//back/div/listBibl/biblStruct");
		baseCitation.nlmPath.
			add("//ref-list/ref"); // note: sometimes we just have the raw citation bellow this!
		baseCitation.pdfxPath.
			add("//ref-list/ref"); // note: there is nothing beyond that in pdfx xml results!
		citationsFields.add(baseCitation);
		// the rest of the citation fields are relative to the base path 

		// title
		FieldSpecification titleField2 = new FieldSpecification();
		titleField2.fieldName = "title";
		titleField2.isTextual = true;
		titleField2.grobidPath.
			add("analytic/title/text()");
		titleField2.nlmPath.
			add("*/article-title//text()");
		citationsFields.add(titleField2);
		citationsLabels.add("title");

		// authors
		FieldSpecification authorField2 = new FieldSpecification();
		authorField2.fieldName = "authors";
		authorField2.isTextual = true;
		authorField2.grobidPath.
			add("analytic/author/persName/surname/text()");
		authorField2.nlmPath.
			add("*//name/surname/text()");
		citationsFields.add(authorField2);
		citationsLabels.add("authors");

		// authors
		FieldSpecification firstAuthorField2 = new FieldSpecification();
		firstAuthorField2.fieldName = "first_author";
		firstAuthorField2.isTextual = true;
		firstAuthorField2.grobidPath.
			add("analytic/author[1]/persName/surname/text()");
		firstAuthorField2.nlmPath.
			add("*//name[1]/surname/text()");
		citationsFields.add(firstAuthorField2);
		citationsLabels.add("first_author");

		// date
		FieldSpecification dateField2 = new FieldSpecification();
		dateField2.fieldName = "date";
		dateField2.grobidPath.
			add("monogr/imprint/date/@when");
		dateField2.nlmPath.
			add("*/year/text()");
		citationsFields.add(dateField2);
		citationsLabels.add("date");

		// monograph title
		FieldSpecification inTitleField2 = new FieldSpecification();
		inTitleField2.fieldName = "inTitle";
		inTitleField2.isTextual = true;
		inTitleField2.grobidPath.
			add("monogr/title/text()");
		inTitleField2.nlmPath.
			add("*/source/text()");
		citationsFields.add(inTitleField2);
		citationsLabels.add("inTitle");

		// volume
		FieldSpecification volumeField = new FieldSpecification();
		volumeField.fieldName = "volume";
		volumeField.grobidPath.
			add("monogr/imprint/biblScope[@unit=\"volume\"]/text()");
		volumeField.nlmPath.
			add("*/volume/text()");
		citationsFields.add(volumeField);
		citationsLabels.add("volume");

		// issue
		FieldSpecification issueField = new FieldSpecification();
		issueField.fieldName = "issue";
		issueField.grobidPath.
			add("monogr/imprint/biblScope[@unit=\"issue\"]/text()");
		issueField.nlmPath.
			add("*/issue/text()");
		citationsFields.add(issueField);
		citationsLabels.add("issue");

		// first page
		FieldSpecification pageField = new FieldSpecification();
		pageField.fieldName = "page";
		pageField.grobidPath.
			add("monogr/imprint/biblScope[@unit=\"page\"]/@from");
		pageField.nlmPath.
			add("*/fpage/text()");
		citationsFields.add(pageField);
		citationsLabels.add("page");

		// publisher
		FieldSpecification publisherField = new FieldSpecification();
		publisherField.fieldName = "publisher";
		publisherField.isTextual = true;
		publisherField.grobidPath.
			add("monogr/imprint/publisher/text()");
		publisherField.nlmPath.
			add("*/publisher-name/text()");
		//citationsFields.add(publisherField);
		//citationsLabels.add("publisher");

		// full text structures
		/*FieldSpecification sectionReferenceField = new FieldSpecification();
		sectionReferenceField.fieldName = "references";
		sectionReferenceField.isTextual = true;
		sectionReferenceField.grobidPath.
			add("//back/div/listBibl/biblStruct//text()");
		sectionReferenceField.nlmPath.
			add("//ref-list/ref//text()");
		fulltextFields.add(sectionReferenceField);
		fulltextLabels.add("references");*/

		FieldSpecification sectionTitleField = new FieldSpecification();
		sectionTitleField.fieldName = "section_title";
		sectionTitleField.isTextual = true;
		sectionTitleField.grobidPath.
			add("//text/body/div/head/text()");
		sectionTitleField.nlmPath.
			add("//body//sec/title/text()");
		fulltextFields.add(sectionTitleField);
		fulltextLabels.add("section_title");

		FieldSpecification referenceMarkerField = new FieldSpecification();
		referenceMarkerField.fieldName = "reference_citation";
		referenceMarkerField.isTextual = true;
		referenceMarkerField.grobidPath.
			add("//ref[@type=\"bibr\"]/text()");
		referenceMarkerField.nlmPath.
			add("//xref[@ref-type=\"bibr\"]/text()");
		fulltextFields.add(referenceMarkerField);
		fulltextLabels.add("reference_citation");

		FieldSpecification referenceFigureField = new FieldSpecification();
		referenceFigureField.fieldName = "reference_figure";
		referenceFigureField.isTextual = true;
		referenceFigureField.grobidPath.
			add("//ref[@type=\"figure\"]/text()");
		referenceFigureField.nlmPath.
			add("//xref[@ref-type=\"fig\"]/text()");
		fulltextFields.add(referenceFigureField);
		fulltextLabels.add("reference_figure");

		FieldSpecification referenceTableField = new FieldSpecification();
		referenceTableField.fieldName = "reference_table";
		referenceTableField.isTextual = true;
		referenceTableField.grobidPath.
			add("//ref[@type=\"table\"]/text()");
		referenceTableField.nlmPath.
			add("//xref[@ref-type=\"table\"]/text()");
		fulltextFields.add(referenceTableField);
		fulltextLabels.add("reference_table");

		FieldSpecification figureTitleField = new FieldSpecification();
		figureTitleField.fieldName = "figure_title";
		figureTitleField.isTextual = true;
		figureTitleField.grobidPath.
			add("//figure[not(@type)]/head/text()");
		figureTitleField.nlmPath.
			add("//fig/label/text()");
		fulltextFields.add(figureTitleField);
		fulltextLabels.add("figure_title");
		
		FieldSpecification figureCaptionField = new FieldSpecification();
		figureCaptionField.fieldName = "figure_caption";
		figureCaptionField.isTextual = true;
		figureCaptionField.grobidPath.
			add("//figure[not(@type)]/figDesc/text()");
		figureCaptionField.nlmPath.
			add("//fig/caption/p/text()");
		fulltextFields.add(figureCaptionField);
		fulltextLabels.add("figure_caption");
		
		FieldSpecification tableTitleField = new FieldSpecification();
		tableTitleField.fieldName = "table_title";
		tableTitleField.isTextual = true;
		tableTitleField.grobidPath.
			add("//figure[@type=\"table\"]/head/text()");
		tableTitleField.nlmPath.
			add("//table-wrap/label/text()");
		fulltextFields.add(tableTitleField);
		fulltextLabels.add("table_title");
		
		FieldSpecification tableCaptionField = new FieldSpecification();
		tableCaptionField.fieldName = "table_caption";
		tableCaptionField.isTextual = true;
		tableCaptionField.grobidPath.
			add("//figure[@type=\"table\"]/figDesc/text()");
		tableCaptionField.nlmPath.
			add("//table-wrap/caption/p/text()");
		fulltextFields.add(tableCaptionField);
		fulltextLabels.add("figure_caption");

		//labels.add("section_title");
		//labels.add("paragraph");
		//labels.add("citation_marker");
		//labels.add("figure_marker");
		//labels.add("table_marker");
		
	}
}