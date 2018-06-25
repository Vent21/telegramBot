import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ExchangeRatesParser {
    private static final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static final String COINMARCKETCAP = "https://api.coinmarketcap.com/v1/ticker/";

    public static String parseExchangeRates(){
        StringBuilder sb = new StringBuilder();
        try {
            JSONArray parseCrypto = (JSONArray) JSONValue.parseWithException(Parser.parseUrl(Parser.createUrl(COINMARCKETCAP)));
            JSONObject parseFiat = (JSONObject) JSONValue.parseWithException(Parser.parseUrl(Parser.createUrl(URL)));
            sb.append("Курс валют на сегодня: " + dateConvert((String) parseFiat.get("Timestamp")) + "\n");
            JSONObject valute = (JSONObject) parseFiat.get("Valute");
            // парсинг фиата
            JSONObject usdToRub = (JSONObject) valute.get("USD");
            JSONObject eurToRub = (JSONObject) valute.get("EUR");
            sb.append("USD/RUB - " + usdToRub.get("Value") + "\n");
            sb.append("EUR/RUB - " + eurToRub.get("Value") + "\n");
            sb.append("\n");
            // парсинг крипты
            JSONObject onePlace = (JSONObject) parseCrypto.get(0);
            JSONObject twoPlace = (JSONObject) parseCrypto.get(1);
            sb.append(onePlace.get("symbol") + "/USD - " + onePlace.get("price_usd") + "\n");
            sb.append(twoPlace.get("symbol") + "/USD - " + twoPlace.get("price_usd") + "\n");
            return sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String dateConvert(String date){
        String result = "";
        String inputString = date.substring(0, 10);
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy");

        try {
            Date currentDate = oldDateFormat.parse(inputString);
            result = newDateFormat.format(currentDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
