package org.vanautrui.octofinsights.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vanautrui.vaquitamvc.VaquitaLogger;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static org.vanautrui.octofinsights.App.yandex_api_key;
import static org.vanautrui.octofinsights.App.yandex_translate_base_url;

public class TranslationService {

    //this has a lang 'ru'|'en'|... as a key and a mapping (english text)->
    //the source language is always english in this app
    private static Map<String, Map<String,String>> translationsCache=new HashMap<>();

    public static synchronized String translateText(String text,String target_lang){
        VaquitaLogger.info("translating to "+target_lang);
        return translate_inner_0(text,"plain",target_lang);
    }

    public static synchronized String translateHTML(String html,String target_lang){
        return translate_inner_0(URLEncoder.encode(html),"html",target_lang);
    }

    private static synchronized String translate_inner_0(String content,String format, String target_lang){
        return translate_inner_1(content,format,target_lang);
    }

    private static synchronized String translate_inner_1(String content,String format, String target_lang){

        //first try to find it in the cache
        if(!translationsCache.containsKey(target_lang)){
            translationsCache.put(target_lang,new HashMap<>());
        }

        if(translationsCache.get(target_lang).containsKey(content)){
            return translationsCache.get(target_lang).get(content);
        }

        try{
            String result = translate_inner_2(content,format,target_lang);

            translationsCache.get(target_lang).put(content,result);

            return result;
        }catch (Exception e){
            VaquitaLogger.error("FATAL ERROR IN TRANSLATION. PROVIDING THE ORIGINAL TEXT.");
            return content;
        }
    }

    private static synchronized String translate_inner_2(String text, String format, String target_lang)throws Exception{
        //String text="hello my friend";

        HttpClient client=new DefaultHttpClient();
        HttpGet req = new HttpGet(yandex_translate_base_url +"?key="+yandex_api_key+"&text="+URLEncoder.encode(text)+"&lang=en-"+target_lang+"&format="+format);

        HttpResponse response = client.execute(req);

        ObjectMapper mapper = new ObjectMapper();

        String text_translated = IOUtils.toString(response.getEntity().getContent());
        System.out.println(text_translated);

        JsonNode node = mapper.readTree(text_translated);
        System.out.println(node.get("text").toString());

        String result= node.get("text").get(0).toString();
        String result2= URLDecoder.decode(result);

        return result2.substring(1,result2.length()-1);
    }
}
