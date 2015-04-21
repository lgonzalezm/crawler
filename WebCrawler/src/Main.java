import java.io.IOException;
import java.sql.SQLException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
 
public class Main {
 
	public static void main(String[] args) throws SQLException, IOException {
		//db.runSql2("TRUNCATE Record;");
		//processPage("http://www.mit.edu");
		/*processPage("http://m.gsmarena.com/apple_iphone_6_plus-reviews-6665.php");*/
		processPage("http://www.gsmarena.com/apple_iphone_6_plus-6665.php");
		processPage("http://www.gsmarena.com/apple_iphone_6-6378.php");
		/*processPage("http://www.gsmarena.com/apple_iphone_5s-5685.php");
		processPage("http://www.gsmarena.com/apple_iphone_5c-5690.php");
		processPage("http://www.gsmarena.com/apple_iphone_5-4910.php");
		processPage("http://www.gsmarena.com/apple_iphone_4s-4212.php");
		processPage("http://www.gsmarena.com/apple_iphone_4-3275.php");
		processPage("http://www.gsmarena.com/apple_iphone_4_cdma-3716.php");
		processPage("http://www.gsmarena.com/apple_iphone_3gs-2826.php");
		processPage("http://www.gsmarena.com/apple_iphone_3g-2424.php");
		processPage("http://www.gsmarena.com/apple_iphone-1827.php");*/
	}
 
	public static void processPage(String URL) throws SQLException, IOException{

			Document doc = Jsoup.connect(URL).get();

			// Obtiene comentarios
			Elements questions = doc.select("a[href]");
			for(Element link: questions){
				if(link.text().contains("Read opinions")){
					System.out.println("Link Comentarios: "+link.attr("abs:href"));
					System.out.println("Comentarios: ");
					System.out.println();
					procesaComentarios(link.attr("abs:href"));
				}		
			}
	}
	
	public static void procesaComentarios(String URL){
		Document doc;
		int contadorComentarios = 0;
		String URLFinal = URL;
		try {
			for(int i=1; i < 6; i++){
				if(i != 1)URLFinal = URL.substring(0, URL.length()-4)+"p"+i+".php";
				doc = Jsoup.connect(URLFinal).get();
				Elements questions = doc.select("div.user-thread");
				for(Element link: questions){
					
					for (Element element : link.select("p.uopin")) {
						contadorComentarios++;
						System.out.println(contadorComentarios+") "+ element.html().replace(element.select("a").toString(), "").replace("<br />", "")	);
						System.out.println();
						
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}