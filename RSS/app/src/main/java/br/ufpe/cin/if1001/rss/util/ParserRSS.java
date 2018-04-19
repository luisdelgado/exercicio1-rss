package br.ufpe.cin.if1001.rss.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import br.ufpe.cin.if1001.rss.domain.ItemRSS;

public class ParserRSS {

    //Testar primeiro com o parser simples para exibir lista de titulos - sem informacao de link
    public static List<String> parserSimples(String rssFeed) throws XmlPullParserException, IOException {
        // pegando instancia da XmlPullParserFactory [singleton]
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // criando novo objeto do tipo XmlPullParser
        XmlPullParser parser = factory.newPullParser();
        // Definindo a entrada do nosso parser - argumento passado como parametro
        parser.setInput(new StringReader(rssFeed));
        // Definindo retorno
        List<String> items = new ArrayList<String>();

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String tag = parser.getName();
                //delimitando que estamos apenas interessados em tags <item>
                if (tag.equals("item")) {
                    String title = "";
                    while (parser.next() != XmlPullParser.END_TAG) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            String tagAberta = parser.getName();
                            //pegando as tags <title>
                            if (tagAberta.equals("title")) {
                                title = parser.nextText();
                                items.add(title);
                            } else {
                                parser.next();
                            }
                            parser.nextTag();
                        }
                    }
                }
            }
        }
        return items;
    }


    //Este metodo faz o parsing de RSS gerando objetos ItemRSS

    public static List<ItemRSS> parse(String rssFeed) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(new StringReader(rssFeed));
        xpp.nextTag();
        return readRss(xpp);
    }

    public static List<ItemRSS> readRss(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        List<ItemRSS> items = new ArrayList<ItemRSS>();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("channel")) {
                items.addAll(readChannel(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static List<ItemRSS> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        List<ItemRSS> items = new ArrayList<ItemRSS>();
        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                items.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return items;
    }

    public static ItemRSS readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String pubDate = null;
        String description = null;
        parser.require(XmlPullParser.START_TAG, null, "item");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readData(parser, "title");
            } else if (name.equals("link")) {
                link = readData(parser, "link");
            } else if (name.equals("pubDate")) {
                pubDate = readData(parser, "pubDate");
            } else if (name.equals("description")) {
                description = readData(parser, "description");
            } else {
                skip(parser);
            }
        }
        ItemRSS result = new ItemRSS(title, link, pubDate, description);
        return result;
    }

    // Processa tags de forma parametrizada no feed.
    public static String readData(XmlPullParser parser, String tag)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return data;
    }

    public static String readText(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

     /**/

}
