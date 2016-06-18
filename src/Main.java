import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String getValue(String key, String xml)
    {
        int start = xml.indexOf(key + "=\"");
        int q1 = xml.indexOf("\"", start)+1;
        int q2 = xml.indexOf("\"", q1);
        return xml.substring(q1,q2);
    }

    public static class XMLRSID{

        public XMLRSID(List<String> values)
        {
            lines = values;
        }

        String rsid()
        {
            return getValue("rsId", lines.get(0));
        }

        String gene()
        {
            for(String s : lines)
                if(s.contains("<FxnSet geneId="))
                    return getValue ("geneId", s);
            return "";
        }

        List<String> lines;
    }

    static void run(String fn, String tag) throws IOException {
        String openTag  = "<"  + tag + " ";
        String closeTag = "</" + tag + ">";

        System.out.println("Open: " + openTag);
        BufferedReader in = new BufferedReader(new FileReader(fn));

        boolean inTag = false;
        List<String> values = new ArrayList<String>(100);

        while(in.ready())
        {
            String line = in.readLine();

            if(inTag)
                if(line.contains(closeTag))
                {
                    XMLRSID rsid = new XMLRSID(values);
                    String gene = rsid.gene();
                    if(gene.length() > 0)
                        System.out.println(rsid.rsid() + "," + gene);
                    values.clear();
                    inTag = false;
                }
                else
                    values.add(line);
            else if(line.contains(openTag))
            {
                inTag = true;
                values.add(line);
            }
        }
    }

    public static void main(String[] args) throws IOException {

        for(String s : args)
            System.out.println(s);

        String fileName = args[0];
//        String fileName = "/Users/erikcorona/Downloads/dbsnp/ds_ch21.xml";
        String tag      = "Rs";
        run(fileName, tag);
    }
}
