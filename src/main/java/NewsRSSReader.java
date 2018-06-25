import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class NewsRSSReader {
    private static final String YANDEX_CHUVASHIA = "https://news.yandex.ru/Cheboksary/index.rss";

    public static String readRSS(){
        String rss  = Parser.parseUrl(Parser.createUrl(YANDEX_CHUVASHIA));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        StringBuilder sb = new StringBuilder();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(new InputSource(new StringReader(rss)));

            NodeList list = document.getElementsByTagName("item");
            int controlLenght = 5;
            if(list.getLength() < controlLenght){
                controlLenght = list.getLength();
            }
            for(int i = 0; i < controlLenght; i++){
                Element element = (Element) list.item(i);
                sb.append("\n");
                sb.append(element.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue() + ":\n");
                sb.append(element.getElementsByTagName("description").item(0).getChildNodes().item(0).getNodeValue() + "\n");
                sb.append("\n");
            }
        } catch (Exception e) {
            return "Новости временно не доступны!";
        }
        return sb.toString();
    }
}
