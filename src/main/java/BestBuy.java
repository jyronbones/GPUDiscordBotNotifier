import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

/**
 * This class contains the methods to scrape 30x series GPUs from Best Buy website and relay to discord
 */
public class BestBuy {
    /**
     * This method scrapes Best Buy 30x series graphics cards from website and displays via an embedded text
     *
     * @param event is the passed event as a command
     * @throws IOException is the exception class that is thrown if IO error
     */
    public void bestBuyScrape(GuildMessageReceivedEvent event) throws IOException {


        String bbUrl = "https://www.bestbuy.ca/en-ca/category/graphics-cards/20397?path=category%253AComputers%2B%2526%2BTablets%253Bcategory%253APC%2BComponents%253Bcategory%253AGraphics%2BCards%253Bcustom0graphicscardmodel%253AGeForce%2BRTX%2B3090%257CGeForce%2BRTX%2B3080%257CGeForce%2BRTX%2B3070%257CGeForce%2BRTX%2B3060%2BTi%257CGeForce%2BRTX%2B3060";

        Document doc = Jsoup.connect(bbUrl).timeout(6000).get();

        Elements body = doc.select(".productListingContainer_3JUbO");

        for (Element t : body.select(".col-xs-12_198le")) // loops through the element which holds the products on the page
        {
            Elements title = t.select(".productItemName_3IZ3c");
            Elements price = t.select(".price_FHDfG.medium_za6t1");
            String link = t.select("a").attr("href");
            Elements inStock = t.select(".container_1DAvI");
            Elements inStock2 = t.select(".marketplaceName_3FG8H");

            if ((title.text().contains("3090") || title.text().contains("3080") || title.text().contains("3070")) && // if item available for purchase
                    (inStock.text().contains("Available") || inStock2.text().equals("Marketplace seller"))) {
                EmbedBuilder info = new EmbedBuilder();
                info.setTitle(title.text());
                info.setDescription("⬇BestBuy⬇");
                info.addField("Price", price.text().replace("+", ""), false);
                info.addField("Website", "https://www.bestbuy.ca" + link, false);
                info.addField("Availability", "In Stock", false);
                info.setThumbnail("https://d2t1xqejof9utc.cloudfront.net/screenshots/pics/2a34a345be3bf4b30229de096bd0b06e/large.jpg");

                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(info.build()).queue();
            }
        }
    }
}
