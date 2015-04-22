import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;
 
 
public class Main {
 
	public static void main(String[] args) throws SQLException, IOException {
		//db.runSql2("TRUNCATE Record;");
		//processPage("http://www.mit.edu");
		/*processPage("http://m.gsmarena.com/apple_iphone_6_plus-reviews-6665.php");*/
		//processPage("http://www.gsmarena.com/apple_iphone_6_plus-6665.php");
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
		Documento doc = new Documento();
		Gson gson = new Gson();
		doc.setAnnio("");
		doc.setBateria("http://www.gsmarena.com/apple_iphone_6-6378.php");
		doc.setCamaraPrimaria("");
		doc.setCamaraSecundaria("");
		doc.setCaracteristica("");
		doc.setComentarios("");
		String json = gson.toJson(doc);
		
		
		
		
		
		char[] charArray ={ 'e', 'n', 't', 'r', 'a', 'r' }; 
		
		Mongo mongoClient = new Mongo("localhost", 27017);  
		
		DB db = mongoClient.getDB("TouchFinder");  
		DBObject dbObject = (DBObject)JSON.parse(json);
		DBCollection collection = db.getCollection("Documentos"); 
		System.out.println(collection.insert(dbObject));
	}
 
	public static void processPage(String URL) throws SQLException, IOException{

			Document doc = Jsoup.connect(URL).get();

			// Obtiene comentarios
			Elements questions = doc.select("a[href]");
			Elements tablasComponentes = doc.select("div#specs-list");
			//System.out.println(tablasComponentes.size());
			
			for(Element link: tablasComponentes){
				
				for (Element tabla : link.select("table")) {
					//System.out.println("Tabla: "+tabla.html());
					List<Element> tipos = tabla.select("td.ttl");
					List<Element> dato = tabla.select("td.nfo");
					
					for (int i=0; i <  tipos.size(); i++) {
						System.out.println(tipos.get(i).html().replace("&nbsp;", "----")+" "+dato.get(i).html());
						System.out.println("");
						System.out.println("");
					}
					System.out.println("");
					System.out.println("");
				}
				if(link.text().contains("Read opinions")){
					System.out.println("Link Comentarios: "+link.attr("abs:href"));
					System.out.println("Comentarios: ");
					System.out.println();
					procesaComentarios(link.attr("abs:href"));
				}
			}
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