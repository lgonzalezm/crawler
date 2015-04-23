import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
	public final static String SEPARADOR = "110011";
	public static void main(String[] args) throws SQLException, IOException {
		//db.runSql2("TRUNCATE Record;");
		//processPage("http://www.mit.edu");
		/*processPage("http://m.gsmarena.com/apple_iphone_6_plus-reviews-6665.php");*/
		//processPage("http://www.gsmarena.com/apple_iphone_6_plus-6665.php");
		//processPage("http://www.gsmarena.com/apple_iphone_6-6378.php");
		/*processPage("http://www.gsmarena.com/apple_iphone_5s-5685.php");
		processPage("http://www.gsmarena.com/apple_iphone_5c-5690.php");
		processPage("http://www.gsmarena.com/apple_iphone_5-4910.php");
		processPage("http://www.gsmarena.com/apple_iphone_4s-4212.php");
		processPage("http://www.gsmarena.com/apple_iphone_4-3275.php");
		processPage("http://www.gsmarena.com/apple_iphone_4_cdma-3716.php");
		processPage("http://www.gsmarena.com/apple_iphone_3gs-2826.php");
		processPage("http://www.gsmarena.com/apple_iphone_3g-2424.php");
		processPage("http://www.gsmarena.com/apple_iphone-1827.php");*/
		
		List<String> urls = new ArrayList<String>();
		List<Documento> documentos = new ArrayList<Documento>();
		urls = getUrls();
		Gson gson = new Gson();
		for (String url : urls) {
			Documento doc = new Documento();
			doc = processPage(url);
			System.out.println(gson.toJson(doc));
			documentos.add(doc);
		}
		
		
		
		
//		Mongo mongoClient = new Mongo("localhost", 27017);  
//		
//		DB db = mongoClient.getDB("TouchFinder");  
//		DBObject dbObject = (DBObject)JSON.parse(json);
//		DBCollection collection = db.getCollection("Documentos"); 
//		System.out.println(collection.insert(dbObject));
	}
 
	public static Documento processPage(String URL) throws SQLException, IOException{
			Documento docu = new Documento();
			Document doc = Jsoup.connect(URL).get();
			// Obtiene comentarios
			Elements questions = doc.select("a[href]");
			Elements tablasComponentes = doc.select("div#specs-list");
			
			for(Element link: tablasComponentes){
				for (Element tabla : link.select("table")) {
					List<Element> tipos = tabla.select("td.ttl");
					List<Element> dato = tabla.select("td.nfo");
					
					for (int i=0; i <  tipos.size(); i++) {
						if(tipos.get(i).html().contains("2G bands") || tipos.get(i).html().contains("3G bands") || tipos.get(i).html().contains("4G bands")){
							System.out.println(dato.get(i).text());
							docu.setNetwork(docu.getNetwork()+" "+dato.get(i).text());
						}
						if(tipos.get(i).html().contains("Primary") || tipos.get(i).html().contains("Secondary")){
							System.out.println(dato.get(i).text());
							if(tipos.get(i).html().contains("Primary"))docu.setCamaraPrimaria(dato.get(i).text());
							if(tipos.get(i).html().contains("Secondary"))docu.setCamaraSecundaria(dato.get(i).text());
						}
						if(tipos.get(i).html().contains("OS") || tipos.get(i).html().contains("CPU") || tipos.get(i).html().contains("GPU") || tipos.get(i).html().contains("Chipset")){
							System.out.println(dato.get(i).text());
							if(tipos.get(i).html().contains("OS"))docu.setSo(dato.get(i).text());
							if(tipos.get(i).html().contains("Chipset"))docu.setProcesador(dato.get(i).text());
							if(tipos.get(i).html().contains("CPU"))docu.setProcesador(docu.getProcesador() +" "+dato.get(i).text());
						}
						if(tipos.get(i).html().contains("Size") || tipos.get(i).html().contains("Resolution")){
							System.out.println(dato.get(i).text());
						}
						if(tipos.get(i).html().contains("Card slot") || tipos.get(i).html().contains("Internal")){
							System.out.println(dato.get(i).text());
						}		
						if(tipos.get(i).html().contains("Talk time") || tipos.get(i).html().contains("Stand-by") || tipos.get(i).html().contains("Music play")){
							System.out.println(dato.get(i).text());
						}
					}
					System.out.println(""); 
				}
			}
			for(Element link: questions){
				if(link.text().contains("Read opinions")){
					System.out.println("Buscando Comentarios: ");
					System.out.println();
					//docu.setComentarios(procesaComentarios(link.attr("abs:href")));;
				}		
			}
			return docu;
	}
	
	public static String procesaComentarios(String URL){
		Document doc;
		String comentarios="";
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
						if(contadorComentarios == 1)comentarios = element.html().replace(element.select("a").toString(), "").replace("<br />", "") + SEPARADOR;
						else comentarios = comentarios + element.html().replace(element.select("a").toString(), "").replace("<br />", "") + SEPARADOR;
						//System.out.println(contadorComentarios+") "+ element.html().replace(element.select("a").toString(), "").replace("<br />", "")	);
						//System.out.println();
						
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return comentarios;
	}
	
	private static List<String> getUrls(){
		List<String> urls = new ArrayList<String>();
		urls.add("http://www.gsmarena.com/samsung_galaxy_tab_3_v-7134.php");
		/*urls.add("http://www.gsmarena.com/samsung_galaxy_tab_a_9_7-7122.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_tab_a_8_0-7121.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_xcover_3-6990.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s6_edge_%28cdma%29-7166.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s6_%28cdma%29-7164.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s6_edge-7079.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s6-6849.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_j1_4g-7034.php");
		urls.add("http://www.gsmarena.com/samsung_z1-6894.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a7_duos-7132.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a7-6763.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_grand_max-6905.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_e7-6879.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_e5-6906.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_core_prime-6716.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a5_duos-6802.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a5-6761.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a3_duos-6803.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_a3-6762.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s5_plus-6748.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_v-6725.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_grand_prime-6708.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_ace_style_lte-6706.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_note_edge-6631.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_note_4-6434.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_tab_active-6659.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_mega_2-6482.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_alpha-6573.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s5_mini_duos-6563.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_avant-6542.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s5_mini-6252.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_ace_4-6479.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_young_2-6480.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_core_ii-6331.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_tab_s_8_4-6439.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_core_lite_lte-6432.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_w-6404.php");
		urls.add("http://www.gsmarena.com/samsung_z-6403.php");
		urls.add("http://www.gsmarena.com/samsung_ativ_se-6245.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s5-6033.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_grand_neo-5958.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_j-5887.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_grand_2-5862.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_express_2-5803.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_light-5786.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_ace_3-5479.php");
		urls.add("http://www.gsmarena.com/samsung_i9300_galaxy_s_iii-4238.php");
		urls.add("http://www.gsmarena.com/samsung_galaxy_s_iii_i747-4803.php");
		urls.add("http://www.gsmarena.com/samsung_focus_2_i667-4745.php");
		
		urls.add("http://www.gsmarena.com/apple_iphone_6_plus-6665.php");
		urls.add("http://www.gsmarena.com/apple_iphone_6-6378.php");
		urls.add("http://www.gsmarena.com/apple_iphone_5s-5685.php");
		urls.add("http://www.gsmarena.com/apple_iphone_5c-5690.php");
		urls.add("http://www.gsmarena.com/apple_iphone_5-4910.php");
		urls.add("http://www.gsmarena.com/apple_iphone_4s-4212.php");
		urls.add("http://www.gsmarena.com/apple_iphone_4-3275.php");
		urls.add("http://www.gsmarena.com/apple_iphone_4_cdma-3716.php");
		urls.add("http://www.gsmarena.com/apple_iphone_3gs-2826.php");
		urls.add("http://www.gsmarena.com/apple_iphone_3g-2424.php");
		urls.add("http://www.gsmarena.com/apple_iphone-1827.php");
		
		urls.add("http://www.gsmarena.com/nokia_lumia_730_dual_sim-6640.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_520-5322.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_630-6232.php");
		urls.add("http://www.gsmarena.com/nokia_xl-6148.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_830-6638.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_635-6254.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_1020-5506.php");
		urls.add("http://www.gsmarena.com/nokia_x-6067.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_735-6639.php");
		urls.add("http://www.gsmarena.com/nokia_x2_dual_sim-6383.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_530-6523.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_720-5321.php");
		urls.add("http://www.gsmarena.com/nokia_n8-3252.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_610-4576.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_820-4968.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_800-4240.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_710-4276.php");
		urls.add("http://www.gsmarena.com/nokia_asha_501-5445.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_630_dual_sim-6255.php");
		urls.add("http://www.gsmarena.com/nokia_n9-3398.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_900-4578.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_icon-5833.php");
		urls.add("http://www.gsmarena.com/nokia_5233-3390.php");
		urls.add("http://www.gsmarena.com/nokia_5800_xpressmusic-2537.php");
		urls.add("http://www.gsmarena.com/nokia_808_pureview-4577.php");
		urls.add("http://www.gsmarena.com/nokia_c5_03-3578.php");
		urls.add("http://www.gsmarena.com/nokia_e7-3545.php");
		urls.add("http://www.gsmarena.com/nokia_c7-3394.php");
		urls.add("http://www.gsmarena.com/nokia_asha_503-5794.php");
		urls.add("http://www.gsmarena.com/nokia_500-4085.php");
		urls.add("http://www.gsmarena.com/nokia_x+-6147.php");
		urls.add("http://www.gsmarena.com/nokia_e6-3717.php");
		urls.add("http://www.gsmarena.com/nokia_asha_230-6149.php");
		urls.add("http://www.gsmarena.com/nokia_asha_502_dual_sim-5795.php");
		urls.add("http://www.gsmarena.com/nokia_x6-2922.php");
		urls.add("http://www.gsmarena.com/nokia_n97-2615.php");
		urls.add("http://www.gsmarena.com/nokia_5230-2909.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_822-5090.php");
		urls.add("http://www.gsmarena.com/nokia_c6_01-3524.php");
		urls.add("http://www.gsmarena.com/nokia_x7_00-3664.php");
		urls.add("http://www.gsmarena.com/nokia_700-4021.php");
		urls.add("http://www.gsmarena.com/nokia_701-4119.php");
		urls.add("http://www.gsmarena.com/nokia_n97_mini-2921.php");
		urls.add("http://www.gsmarena.com/nokia_603-4243.php");
		urls.add("http://www.gsmarena.com/nokia_c5_05-4223.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_505-5150.php");
		urls.add("http://www.gsmarena.com/nokia_c5_06-4220.php");
		urls.add("http://www.gsmarena.com/nokia_5235_comes_with_music-3056.php");
		urls.add("http://www.gsmarena.com/nokia_c7_astound-3885.php");
		urls.add("http://www.gsmarena.com/nokia_5800_navigation_edition-2906.php");
		urls.add("http://www.gsmarena.com/vertu_constellation-4263.php");
		urls.add("http://www.gsmarena.com/nokia_703-4133.php");
		urls.add("http://www.gsmarena.com/nokia_800c-4662.php");
		urls.add("http://www.gsmarena.com/nokia_lumia_710_t_mobile-4372.php");
		urls.add("http://www.gsmarena.com/nokia_c5_04-4222.php");
		
		urls.add("http://www.gsmarena.com/lg_aka-7148.php");
		urls.add("http://www.gsmarena.com/lg_magna-7051.php");
		urls.add("http://www.gsmarena.com/lg_spirit-7052.php");
		urls.add("http://www.gsmarena.com/lg_leon-7053.php");
		urls.add("http://www.gsmarena.com/lg_joy-7054.php");
		urls.add("http://www.gsmarena.com/lg_g_flex2-6916.php");
		urls.add("http://www.gsmarena.com/lg_tribute-6812.php");
		urls.add("http://www.gsmarena.com/lg_l_prime-6775.php");
		urls.add("http://www.gsmarena.com/lg_g2_lite-6776.php");
		urls.add("http://www.gsmarena.com/lg_g3_dual_lte-6764.php");
		urls.add("http://www.gsmarena.com/lg_g3_screen-6750.php");
		urls.add("http://www.gsmarena.com/lg_f60-6709.php");
		urls.add("http://www.gsmarena.com/lg_l60-6606.php");
		urls.add("http://www.gsmarena.com/lg_l60_dual-6415.php");
		urls.add("http://www.gsmarena.com/lg_g3_stylus-6555.php");
		urls.add("http://www.gsmarena.com/lg_l_bello-6593.php");
		urls.add("http://www.gsmarena.com/lg_l_fino-6592.php");
		urls.add("http://www.gsmarena.com/lg_g_pad_8_0_lte-6575.php");
		urls.add("http://www.gsmarena.com/lg_g_vista-6569.php");
		urls.add("http://www.gsmarena.com/lg_g3_a-6562.php");
		urls.add("http://www.gsmarena.com/lg_l50-6546.php");
		urls.add("http://www.gsmarena.com/lg_l30-6547.php");
		urls.add("http://www.gsmarena.com/lg_l20-6548.php");
		urls.add("http://www.gsmarena.com/lg_g3_s-6475.php");
		urls.add("http://www.gsmarena.com/lg_l65_d280-6315.php");
		urls.add("http://www.gsmarena.com/lg_g3-6294.php");
		urls.add("http://www.gsmarena.com/lg_l35-6366.php");
		urls.add("http://www.gsmarena.com/lg_volt-6357.php");
		urls.add("http://www.gsmarena.com/lg_l80_dual-6333.php");
		urls.add("http://www.gsmarena.com/lg_lucid_3_vs876-6326.php");
		urls.add("http://www.gsmarena.com/lg_l65_dual_d285-6307.php");
		urls.add("http://www.gsmarena.com/lg_f70_d315-6133.php");
		urls.add("http://www.gsmarena.com/lg_g2_mini-6077.php");
		urls.add("http://www.gsmarena.com/lg_l90_d405-6100.php");
		urls.add("http://www.gsmarena.com/lg_l70_d320n-6285.php");
		urls.add("http://www.gsmarena.com/lg_l40_d160-6286.php");
		urls.add("http://www.gsmarena.com/lg_g_pro_2-6052.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l1_ii_tri_e475-6032.php");
		urls.add("http://www.gsmarena.com/lg_gx_f310l-5901.php");
		urls.add("http://www.gsmarena.com/lg_nexus_5-5705.php");
		urls.add("http://www.gsmarena.com/lg_g_flex-5806.php");
		urls.add("http://www.gsmarena.com/lg_g_pro_lite-5772.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l2_ii_e435-5750.php");
		urls.add("http://www.gsmarena.com/lg_g2-5543.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l9_ii-5650.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l4_ii_e440-5487.php");
		urls.add("http://www.gsmarena.com/lg_optimus_f3-5509.php");
		urls.add("http://www.gsmarena.com/lg_lucid2_vs870-5390.php");
		urls.add("http://www.gsmarena.com/lg_optimus_f7-5315.php");
		urls.add("http://www.gsmarena.com/lg_optimus_f6-5722.php");
		urls.add("http://www.gsmarena.com/lg_optimus_f5-5316.php");
		urls.add("http://www.gsmarena.com/lg_optimus_g_pro_e985-5254.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l7_ii_dual_p715-5372.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l7_ii_p710-5275.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l5_ii_dual_e455-5373.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l5_ii_e460-5293.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l3_ii_dual_e435-5389.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l3_ii_e430-5292.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l1_ii_e410-5608.php");
		urls.add("http://www.gsmarena.com/lg_nexus_4_e960-5048.php");
		urls.add("http://www.gsmarena.com/lg_spectrum_ii_4g_vs930-5078.php");
		urls.add("http://www.gsmarena.com/lg_mach_ls860-5032.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l9_p769-5025.php");
		urls.add("http://www.gsmarena.com/lg_optimus_vu_ii-5325.php");
		urls.add("http://www.gsmarena.com/lg_optimus_vu_ii_f200-4950.php");
		urls.add("http://www.gsmarena.com/lg_optimus_g_e970-5062.php");
		urls.add("http://www.gsmarena.com/lg_optimus_g_ls970-4751.php");
		urls.add("http://www.gsmarena.com/lg_optimus_g_e975-4941.php");
		urls.add("http://www.gsmarena.com/lg_intuition_vs950-5005.php");
		urls.add("http://www.gsmarena.com/lg_splendor_us730-4982.php");
		urls.add("http://www.gsmarena.com/lg_escape_p870-4981.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l5_dual_e615-4964.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l9_p760-4955.php");
		urls.add("http://www.gsmarena.com/lg_motion_4g_ms770-4948.php");
		urls.add("http://www.gsmarena.com/lg_optimus_vu_p895-4932.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l3_e405-4835.php");
		urls.add("http://www.gsmarena.com/lg_optimus_elite_ls696-4749.php");
		urls.add("http://www.gsmarena.com/lg_optimus_lte2-4739.php");
		urls.add("http://www.gsmarena.com/lg_optimus_true_hd_lte_p936-4717.php");
		urls.add("http://www.gsmarena.com/lg_lucid_4g_vs840-4653.php");
		urls.add("http://www.gsmarena.com/lg_optimus_m+_ms695-4649.php");
		urls.add("http://www.gsmarena.com/lg_optimus_4x_hd_p880-4563.php");
		urls.add("http://www.gsmarena.com/lg_optimus_3d_max_p720-4562.php");
		urls.add("http://www.gsmarena.com/lg_optimus_3d_cube_su870-4545.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l7_p700-4565.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l5_e610-4571.php");
		urls.add("http://www.gsmarena.com/lg_optimus_vu_f100s-4554.php");
		urls.add("http://www.gsmarena.com/lg_optimus_lte_tag-4555.php");
		urls.add("http://www.gsmarena.com/lg_optimus_l3_e400-4461.php");
		urls.add("http://www.gsmarena.com/lg_connect_4g_ms840-4430.php");
		urls.add("http://www.gsmarena.com/lg_viper_4g_lte_ls840-4416.php");
		urls.add("http://www.gsmarena.com/lg_spectrum_vs920-4377.php");
		urls.add("http://www.gsmarena.com/lg_prada_3_0-4064.php");
		urls.add("http://www.gsmarena.com/lg_nitro_hd-4329.php");
		urls.add("http://www.gsmarena.com/lg_optimus_4g_lte_p935-4353.php");
		urls.add("http://www.gsmarena.com/lg_optimus_2_as680-4402.php");
		urls.add("http://www.gsmarena.com/lg_doubleplay-4257.php");
		urls.add("http://www.gsmarena.com/lg_enlighten_vs700-4352.php");
		urls.add("http://www.gsmarena.com/lg_jil_sander_mobile-4221.php");
		urls.add("http://www.gsmarena.com/lg_optimus_slider-4286.php");
		urls.add("http://www.gsmarena.com/lg_optimus_lte_su640-4213.php");
		urls.add("http://www.gsmarena.com/lg_optimus_lte_lu6200-4188.php");
		urls.add("http://www.gsmarena.com/lg_optimus_ex_su880-4201.php");
		urls.add("http://www.gsmarena.com/lg_optimus_q2_lu6500-4140.php");
		urls.add("http://www.gsmarena.com/lg_optimus_hub_e510-4157.php");
		urls.add("http://www.gsmarena.com/lg_optimus_sol_e730-4067.php");
		urls.add("http://www.gsmarena.com/lg_optimus_net_dual-4309.php");
		urls.add("http://www.gsmarena.com/lg_optimus_net-4043.php");
		urls.add("http://www.gsmarena.com/lg_esteem_ms910-4289.php");
		urls.add("http://www.gsmarena.com/lg_marquee_ls855-4287.php");
		urls.add("http://www.gsmarena.com/lg_optimus_black_%28white_version%29-4037.php");
		urls.add("http://www.gsmarena.com/lg_optimus_pro_c660-4040.php");
		urls.add("http://www.gsmarena.com/lg_optimus_big_lu6800-3937.php");
		urls.add("http://www.gsmarena.com/lg_us760_genesis-4033.php");
		urls.add("http://www.gsmarena.com/lg_phoenix_p505-3924.php");
		urls.add("http://www.gsmarena.com/lg_thrive_p506-3918.php");
		urls.add("http://www.gsmarena.com/lg_thrill_4g_p925-3886.php");
		urls.add("http://www.gsmarena.com/lg_optimus_3d_p920-3759.php");
		urls.add("http://www.gsmarena.com/lg_optimus_chat_c550-3837.php");
		urls.add("http://www.gsmarena.com/lg_optimus_me_p350-3735.php");
		urls.add("http://www.gsmarena.com/lg_optimus_black_p970-3704.php");
		urls.add("http://www.gsmarena.com/lg_optimus_2x_su660-3762.php");
		urls.add("http://www.gsmarena.com/lg_optimus_2x-3598.php");
		urls.add("http://www.gsmarena.com/lg_optimus_mach_lu3000-3667.php");
		urls.add("http://www.gsmarena.com/lg_revolution-3732.php");
		urls.add("http://www.gsmarena.com/lg_axis-3731.php");
		urls.add("http://www.gsmarena.com/lg_apex-3632.php");
		urls.add("http://www.gsmarena.com/lg_vortex_vs660-3630.php");
		urls.add("http://www.gsmarena.com/lg_quantum-3558.php");
		urls.add("http://www.gsmarena.com/lg_c900_optimus_7q_-3555.php");
		urls.add("http://www.gsmarena.com/lg_e900_optimus_7-3532.php");
		urls.add("http://www.gsmarena.com/lg_optimus_chic_e720-3494.php");
		urls.add("http://www.gsmarena.com/lg_optimus_m-3669.php");
		urls.add("http://www.gsmarena.com/lg_optimus_s-3583.php");
		urls.add("http://www.gsmarena.com/lg_optimus_t-3544.php");
		urls.add("http://www.gsmarena.com/lg_optimus_one_p500-3516.php");
		
		urls.add("http://www.gsmarena.com/sony_xperia_m4_aqua_dual-7100.php");
		urls.add("http://www.gsmarena.com/sony_xperia_m4_aqua-7068.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4g-7059.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4g_dual-7060.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4g_dual-7060.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4g_dual-7060.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4_dual-7031.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e4-6882.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e3_dual-6663.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e3-6634.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z3_dual-6632.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z3v-6796.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z3-6539.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z3_compact-6538.php");
		urls.add("http://www.gsmarena.com/sony_xperia_m2_aqua-6582.php");
		urls.add("http://www.gsmarena.com/sony_xperia_c3_dual-6489.php");
		urls.add("http://www.gsmarena.com/sony_xperia_c3-6488.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z2a-6456.php");
		urls.add("http://www.gsmarena.com/sony_xperia_t3-6408.php");
		urls.add("http://www.gsmarena.com/sony_xperia_m2-6146.php");
		urls.add("http://www.gsmarena.com/sony_xperia_m2_dual-6162.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z2-6144.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e1_dual-5967.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e1-5966.php");
		urls.add("http://www.gsmarena.com/sony_xperia_t2_ultra_dual-5968.php");
		urls.add("http://www.gsmarena.com/sony_xperia_t2_ultra-5965.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z1s-5950.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z1_compact-5753.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z1-5596.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z_ultra-5540.php");
		urls.add("http://www.gsmarena.com/sony_xperia_c-5541.php");
		urls.add("http://www.gsmarena.com/sony_xperia_m-5497.php");
		urls.add("http://www.gsmarena.com/sony_xperia_zr-5421.php");
		urls.add("http://www.gsmarena.com/sony_xperia_l-5363.php");
		urls.add("http://www.gsmarena.com/sony_xperia_sp-5364.php");
		urls.add("http://www.gsmarena.com/sony_xperia_z-5204.php");
		urls.add("http://www.gsmarena.com/sony_xperia_zl-5203.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e_dual-5148.php");
		urls.add("http://www.gsmarena.com/sony_xperia_e-5149.php");
		urls.add("http://www.gsmarena.com/sony_xperia_t_lte-5015.php");
		urls.add("http://www.gsmarena.com/sony_xperia_v-4958.php");
		urls.add("http://www.gsmarena.com/sony_xperia_j-4930.php");
		urls.add("http://www.gsmarena.com/sony_xperia_tx-4959.php");
		urls.add("http://www.gsmarena.com/sony_xperia_sl-4894.php");
		urls.add("http://www.gsmarena.com/sony_xperia_tipo-4718.php");
		urls.add("http://www.gsmarena.com/sony_xperia_go-4782.php");
		
		urls.add("http://www.gsmarena.com/htc_one_m9-6891.php");
		urls.add("http://www.gsmarena.com/htc_one_m9+-6977.php");
		urls.add("http://www.gsmarena.com/htc_desire_820-6636.php");
		urls.add("http://www.gsmarena.com/htc_one_%28m8%29-6074.php");
		urls.add("http://www.gsmarena.com/htc_desire_826_dual_sim-6921.php");
		urls.add("http://www.gsmarena.com/htc_desire_816-6073.php");
		urls.add("http://www.gsmarena.com/htc_desire_620g_dual_sim-6841.php");
		urls.add("http://www.gsmarena.com/htc_desire_816g_dual_sim-6736.php");
		urls.add("http://www.gsmarena.com/htc_desire_626g+-7183.php");
		urls.add("http://www.gsmarena.com/htc_desire_eye-6715.php");
		urls.add("http://www.gsmarena.com/htc_desire_616_dual_sim-6234.php");
		urls.add("http://www.gsmarena.com/htc_one_m8s-7165.php");
		urls.add("http://www.gsmarena.com/htc_one_e9+-7111.php");
		urls.add("http://www.gsmarena.com/htc_desire_510-6608.php");
		urls.add("http://www.gsmarena.com/htc_desire_820s_dual_sim-7115.php");
		urls.add("http://www.gsmarena.com/htc_one_%28e8%29-6397.php");
		urls.add("http://www.gsmarena.com/htc_desire_516_dual_sim-6463.php");
		urls.add("http://www.gsmarena.com/htc_one_x-4320.php");
		urls.add("http://www.gsmarena.com/htc_one_mini_2-6345.php");
		urls.add("http://www.gsmarena.com/htc_desire_610-6160.php");
		urls.add("http://www.gsmarena.com/htc_one_max-5704.php");
		urls.add("http://www.gsmarena.com/htc_desire_310-6009.php");
		urls.add("http://www.gsmarena.com/htc_desire_820q_dual_sim-6854.php");
		urls.add("http://www.gsmarena.com/htc_butterfly-5118.php");
		urls.add("http://www.gsmarena.com/htc_desire_500-5623.php");
		urls.add("http://www.gsmarena.com/htc_desire_x-4951.php");
		urls.add("http://www.gsmarena.com/htc_desire_700_dual_sim-5870.php");
		urls.add("http://www.gsmarena.com/htc_droid_dna-5113.php");
		urls.add("http://www.gsmarena.com/htc_butterfly_s-5537.php");
		urls.add("http://www.gsmarena.com/htc_windows_phone_8x-4975.php");
		urls.add("http://www.gsmarena.com/htc_one_v-4575.php");
		urls.add("http://www.gsmarena.com/htc_droid_incredible_4g_lte-4744.php");
		urls.add("http://www.gsmarena.com/htc_vivid-4302.php");
		urls.add("http://www.gsmarena.com/htc_desire_612-6719.php");
		urls.add("http://www.gsmarena.com/htc_desire_310_dual_sim-6348.php");
		urls.add("http://www.gsmarena.com/htc_one_remix-6528.php");
		urls.add("http://www.gsmarena.com/htc_sensation-3875.php");
		urls.add("http://www.gsmarena.com/htc_one_sv-5105.php");
		urls.add("http://www.gsmarena.com/htc_desire_hd-3468.php");
		urls.add("http://www.gsmarena.com/htc_rezound-4099.php");
		urls.add("http://www.gsmarena.com/htc_radar-4131.php");
		urls.add("http://www.gsmarena.com/htc_wildfire_s-3777.php");
		urls.add("http://www.gsmarena.com/htc_rhyme-4132.php");
		urls.add("http://www.gsmarena.com/htc_desire_c-4759.php");
		urls.add("http://www.gsmarena.com/htc_desire_501_dual_sim-5907.php");
		urls.add("http://www.gsmarena.com/htc_explorer-4102.php");
		urls.add("http://www.gsmarena.com/htc_sensation_xe-4164.php");
		urls.add("http://www.gsmarena.com/htc_desire_700-6552.php");
		urls.add("http://www.gsmarena.com/htc_titan_ii-4411.php");
		urls.add("http://www.gsmarena.com/htc_incredible_s-3788.php");
		urls.add("http://www.gsmarena.com/htc_one_s_c2-4836.php");
		urls.add("http://www.gsmarena.com/htc_evo_4g-3427.php");
		
		urls.add("http://www.gsmarena.com/motorola_moto_e_%282015%29-6986.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g_4g_%282nd_gen%29-7137.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g_4g_dual_sim_%282nd_gen%29-6991.php");
		urls.add("http://www.gsmarena.com/motorola_moto_maxx-6779.php");
		urls.add("http://www.gsmarena.com/motorola_droid_turbo-6727.php");
		urls.add("http://www.gsmarena.com/motorola_nexus_6-6604.php");
		urls.add("http://www.gsmarena.com/motorola_moto_x_%282014%29-6649.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g_dual_sim_%282nd_gen%29-6648.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g_%282nd_gen%29-6647.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g_4g-6355.php");
		urls.add("http://www.gsmarena.com/motorola_luge-6806.php");
		urls.add("http://www.gsmarena.com/motorola_moto_e-6376.php");
		urls.add("http://www.gsmarena.com/motorola_moto_e_dual_sim-6323.php");
		urls.add("http://www.gsmarena.com/motorola_moto_g-5831.php");
		urls.add("http://www.gsmarena.com/motorola_moto_x-5601.php");
		urls.add("http://www.gsmarena.com/motorola_droid_ultra-5605.php");
		urls.add("http://www.gsmarena.com/motorola_droid_maxx-5604.php");
		urls.add("http://www.gsmarena.com/motorola_droid_mini-5603.php");
		urls.add("http://www.gsmarena.com/motorola_razr_d3_xt919-5356.php");
		urls.add("http://www.gsmarena.com/motorola_razr_d1-5355.php");
		urls.add("http://www.gsmarena.com/motorola_electrify_m_xt905-5102.php");
		urls.add("http://www.gsmarena.com/motorola_razr_i_xt890-4998.php");
		urls.add("http://www.gsmarena.com/motorola_droid_razr_maxx_hd-4972.php");
		urls.add("http://www.gsmarena.com/motorola_droid_razr_hd-4971.php");
		urls.add("http://www.gsmarena.com/motorola_razr_hd_xt925-4970.php");
		urls.add("http://www.gsmarena.com/motorola_droid_razr_m-4973.php");
		urls.add("http://www.gsmarena.com/motorola_razr_m_xt905-4974.php");
		urls.add("http://www.gsmarena.com/motorola_defy_xt_xt556-4892.php");
		urls.add("http://www.gsmarena.com/motorola_electrify_2_xt881-4891.php");
		urls.add("http://www.gsmarena.com/motorola_photon_q_4g_lte_xt897-4885.php");
		urls.add("http://www.gsmarena.com/motorola_defy_pro_xt560-4868.php");
		urls.add("http://www.gsmarena.com/motorola_atrix_hd_mb886-4867.php");
		urls.add("http://www.gsmarena.com/motorola_xt760-4820.php");
		urls.add("http://www.gsmarena.com/motorola_atrix_tv_xt687-4921.php");
		urls.add("http://www.gsmarena.com/motorola_atrix_tv_xt682-4797.php");
		
		urls.add("http://www.gsmarena.com/motorola_motosmart_me_xt303-5153.php");
		urls.add("http://www.gsmarena.com/motorola_razr_v_xt885-5224.php");
		urls.add("http://www.gsmarena.com/motorola_razr_v_xt889-4856.php");
		urls.add("http://www.gsmarena.com/motorola_razr_v_mt887-4778.php");
		urls.add("http://www.gsmarena.com/motorola_motosmart_mix_xt550-4762.php");
		urls.add("http://www.gsmarena.com/motorola_xt390-4696.php");
		urls.add("http://www.gsmarena.com/motorola_razr_maxx-4666.php");
		urls.add("http://www.gsmarena.com/motorola_defy_xt535-4622.php");
		urls.add("http://www.gsmarena.com/motorola_droid_4_xt894-4418.php");
		urls.add("http://www.gsmarena.com/motorola_droid_razr_maxx-4417.php");
		urls.add("http://www.gsmarena.com/motorola_motoluxe_mt680-4849.php");
		urls.add("http://www.gsmarena.com/motorola_motoluxe_xt389-4848.php");
		urls.add("http://www.gsmarena.com/motorola_motoluxe-4404.php");
		urls.add("http://www.gsmarena.com/motorola_defy_mini_xt321-4847.php");
		urls.add("http://www.gsmarena.com/motorola_defy_mini_xt320-4375.php");
		urls.add("http://www.gsmarena.com/motorola_xt319-4918.php");
		urls.add("http://www.gsmarena.com/motorola_fire-4386.php");
		urls.add("http://www.gsmarena.com/motorola_mt917-4383.php");
		urls.add("http://www.gsmarena.com/motorola_xt928-4382.php");
		urls.add("http://www.gsmarena.com/motorola_xt532-4355.php");
		urls.add("http://www.gsmarena.com/motorola_moto_xt615-4348.php");
		urls.add("http://www.gsmarena.com/motorola_razr_xt910-4273.php");
		urls.add("http://www.gsmarena.com/motorola_droid_razr_xt912-4101.php");
		urls.add("http://www.gsmarena.com/motorola_atrix_2_mb865-4199.php");
		urls.add("http://www.gsmarena.com/motorola_admiral_xt603-4282.php");
		urls.add("http://www.gsmarena.com/motorola_me632-4272.php");
		urls.add("http://www.gsmarena.com/motorola_pro+-4128.php");
		urls.add("http://www.gsmarena.com/motorola_defy+-4098.php");
		urls.add("http://www.gsmarena.com/motorola_fire_xt-4160.php");
		urls.add("http://www.gsmarena.com/motorola_fire_xt311-4045.php");
		urls.add("http://www.gsmarena.com/motorola_spice_key_xt317-4209.php");
		urls.add("http://www.gsmarena.com/motorola_spice_key-4046.php");
		urls.add("http://www.gsmarena.com/motorola_milestone_3_xt860-4349.php");
		urls.add("http://www.gsmarena.com/motorola_droid_3-4036.php");
		urls.add("http://www.gsmarena.com/motorola_triumph-3988.php");
		urls.add("http://www.gsmarena.com/motorola_photon_4g_mb855-3987.php");
		urls.add("http://www.gsmarena.com/motorola_moto_mt870-4023.php");
		urls.add("http://www.gsmarena.com/motorola_moto_mt620-3982.php");
		urls.add("http://www.gsmarena.com/motorola_milestone_xt883-4025.php");
		urls.add("http://www.gsmarena.com/motorola_moto_xt882-3981.php");
		urls.add("http://www.gsmarena.com/motorola_moto_xt316-4024.php");
		urls.add("http://www.gsmarena.com/motorola_xprt_mb612-3946.php");
		urls.add("http://www.gsmarena.com/motorola_pro-3781.php");
		urls.add("http://www.gsmarena.com/motorola_atrix-3709.php");
		urls.add("http://www.gsmarena.com/motorola_atrix_4g-3708.php");
		urls.add("http://www.gsmarena.com/motorola_cliq_2-3454.php");
		urls.add("http://www.gsmarena.com/motorola_droid_bionic_xt875-3710.php");
		urls.add("http://www.gsmarena.com/motorola_droid_x_me811-3730.php");
		urls.add("http://www.gsmarena.com/motorola_droid_bionic_xt865-4523.php");
		urls.add("http://www.gsmarena.com/motorola_moto_me525-3912.php");
		urls.add("http://www.gsmarena.com/motorola_milestone_2_me722-3911.php");
		urls.add("http://www.gsmarena.com/motorola_droid_2_global-3636.php");
		urls.add("http://www.gsmarena.com/motorola_droid_pro_xt610-3541.php");
		urls.add("http://www.gsmarena.com/motorola_xt301-3628.php");
		urls.add("http://www.gsmarena.com/motorola_spice_xt300-3540.php");
		urls.add("http://www.gsmarena.com/motorola_flipside_mb508-3539.php");
		urls.add("http://www.gsmarena.com/motorola_moto_mt716-3535.php");
		urls.add("http://www.gsmarena.com/motorola_bravo_mb520-3538.php");
		urls.add("http://www.gsmarena.com/motorola_citrus_wx445-3542.php");
		urls.add("http://www.gsmarena.com/motorola_defy-3514.php");
		urls.add("http://www.gsmarena.com/motorola_milestone_2-3495.php");
		urls.add("http://www.gsmarena.com/motorola_droid_2-3475.php");
		urls.add("http://www.gsmarena.com/motorola_mt810lx-3626.php");
		urls.add("http://www.gsmarena.com/motorola_xt810-3521.php");
		urls.add("http://www.gsmarena.com/motorola_xt806-3476.php");*/
		return urls;
	}
}