import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Parsing {
	public static void main(String[] args) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        File f = new File("WebContent/mains243.xml");
        SaxHandler dh = new SaxHandler();
        parser.parse(f, dh);
        
        SAXParserFactory factory2 = SAXParserFactory.newInstance();
        SAXParser parser2 = factory2.newSAXParser();
        File inputfile2 = new File("WebContent/actors63.xml");
        SaxActor movie2 = new SaxActor();
        parser2.parse(inputfile2, movie2);
        
        SAXParserFactory factory1 = SAXParserFactory.newInstance();
        SAXParser parser1 = factory1.newSAXParser();
        File inputfile1 = new File("WebContent/casts124.xml");
        SaxCast movie1 = new SaxCast();
        parser1.parse(inputfile1, movie1);
        
    }
}