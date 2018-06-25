package afishaParsers.teatre;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TheatreParser {
    private static final String THEATRE_AFISHA = "https://afisha.cheb.ru/teatr/";

    public static String getTheatreAfisha(){
        StringBuilder sb = new StringBuilder("Театральная афиша:\n" + " \n");
        try {
            if(!getSpectaclesInWebPage().isEmpty()){
                for (Spectacle v : getSpectaclesInWebPage()) {
                    sb.append(v.toString() + "\n");
                    sb.append(" \n");
                }
            }
        } catch (IOException e) {
            return "Невозможно отобразить афишу\n" + e.toString();
        }
        return sb.toString();
    }

    private static List<Spectacle> getSpectaclesInWebPage() throws IOException {
        List<Spectacle> spectacles = new ArrayList<>();
            Document doc = Jsoup.connect(THEATRE_AFISHA).get();

            Elements elements = doc.getElementsByClass("dateoneteatr");
            for (Element element : elements) {
                //Парсим дату спектакля
                Element dateElement = element.getElementsByClass("dayteatr").first();
                String dayNumber = dateElement.ownText();
                String month = dateElement.getElementsByTag("div").get(1).ownText();
                String dayName = dateElement.select("span").first().ownText();

                String date = String.format("%s (%s) %s", dayNumber, dayName, month);

                //Парсим сам спектакль
                Element timeElement = element.getElementsByClass("teatrrow1_").first();
                String time = timeElement.ownText();

                String name = element.getElementsByClass("teatrrow2").first().getElementsByTag("a").first().ownText();
                String description = element.getElementsByClass("teatrrow2").first().getElementsByTag("div").get(2).ownText();
                String teatreName = element.getElementsByClass("teatrrow2_").first().getElementsByTag("a").first().ownText();

                String cost = element.getElementsByClass("teatrrow4").select("div").get(1).ownText();

                spectacles.add(new Spectacle(date, time, name, description, teatreName, cost));
                //если в день больше одного спектакля
                if(element.select("div[class=teatrrow1_ teatrtopline]").size() > 0){
                    Elements timeElements = element.select("div[class=teatrrow1_ teatrtopline]");
                    Elements nameAndDescription = element.select("div[class=teatrrow2 teatrtopline]");
                    Elements teatreNamesElements = elements.select("div[class=teatrrow2_ teatrtopline]");
                    Elements costElements = element.select("div[class=teatrrow4 teatrtopline]");

                    for(int i = 0; i < timeElements.size(); i++){
                        String otherTime = timeElements.get(i).ownText();
                        String otherName = nameAndDescription.get(i).getElementsByTag("a").first().ownText();
                        String otherDescription = nameAndDescription.get(i).select("div[style=\"clear:both\"]").first().ownText();
                        String otherTeatreName = teatreNamesElements.get(i).getElementsByTag("a").first().ownText();
                        String otherCost = costElements.get(i).select("div").get(1).ownText();
                        spectacles.add(new Spectacle(date, otherTime, otherName, otherDescription, otherTeatreName, otherCost));
                    }
                }
            }
        return spectacles;
    }
}
