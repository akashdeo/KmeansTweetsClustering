package KmeansTweets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONObject;
/**
 *
 * @author Akash-Deo
 * net-id:apd160330
 * K means tweets
 * 
 */
public class KmeansTweets {
    static String noOfClusters1;
    static String KmeansTweets;
    static String initialCentroids;
    static String InputFile;
    static String OutputFile;
    public static void main(String[] args) throws FileNotFoundException, IOException {
        KmeansTweets=args[0];
        noOfClusters1=args[1];
        initialCentroids=args[2];
        InputFile=args[3];
        OutputFile=args[4];
        String outputFileName = OutputFile;
        
        FileWriter fstream = new FileWriter(outputFileName);
        BufferedWriter out = new BufferedWriter(fstream);
        //JSONParser parser=new JSONParser();
        File f=new File(InputFile);
        FileReader fr=new FileReader(f);
        BufferedReader br=new BufferedReader(fr);
        String line="";
        int lineCount=0;
        ArrayList<Tweets> arraylist=new ArrayList<>();
        while((line=br.readLine())!=null)
        {
            lineCount++;
            String split[]=line.split("\"text\"");
            String split1[]=line.split("\"id\":");
            String text = split[1].substring(3,split[1].indexOf("\","));
            long id = Long.parseLong(split1[1].substring(1, split1[1].indexOf(',')));
            Tweets tweet=new Tweets(text,id);
            arraylist.add(tweet);
        }
        br.close();
        line="";
         File f1=new File(initialCentroids);
        fr=new FileReader(f1);
        br=new BufferedReader(fr);
        
        String distanceMatrix[][]=new String[lineCount+1][Integer.parseInt(noOfClusters1)+2];
        int y=1;

        ArrayList<Tweets> arr=new ArrayList<>();
        while((line=br.readLine())!=null)
        {
            String line1 = "";
            for(int i=0;i<line.length();i++)
            {
                if(line.charAt(i)!=',')
                   line1=line1+line.charAt(i);
            }
            long l=Long.parseLong(line1);
            for(Tweets t:arraylist)
            {
                if(t.id==l)
                {
                    Tweets tw=new Tweets(t.text,l);
                    arr.add(tw);
                }
            }
        }
        distanceMatrix[0][0]="";
        int u=1;
        ArrayList<Long> fg=new ArrayList<>();
        for(Tweets p:arr)
        {
            distanceMatrix[0][u]=""+p.id;
            u++;
            fg.add(p.id);
        }
        u=1;
        
        for(Tweets p:arraylist)
        {
            distanceMatrix[u][0]=""+p.id;
            
            u++;
        }
        ArrayList<Long> abcd=new ArrayList<>();
        //u=1;
        ArrayList<ArrayList<Long>> arrayList = null;
        int flag=0;
        double sum=0.0;
        for(int v=0;v<25;v++)
        {
            double squaredError=0.0;
            ArrayList<Long> m=new ArrayList<>();
            m=abcd;
          Collections.sort(abcd);
          Collections.sort(fg);
          if(fg.equals(abcd))
          {
              out.write("breaks after "+(v+1)+"iterations"+"\r\n");
              out.flush();
              out.write("\r\n");
             break;
          }
          fg=m;
          abcd=new ArrayList<>();
        arrayList=new ArrayList<>();
        
        for(int i=1;i<=arraylist.size();i++)
        {
            double min=Double.MAX_VALUE;
            long id = 0;
            for(int j=1;j<=Integer.parseInt(noOfClusters1);j++)
            {
                String id1=distanceMatrix[i][0];
                String id2=distanceMatrix[0][j];
                String tweet1="";
                String tweet2="";
                for(Tweets t:arraylist)
                {
                    
                    long l=t.id;
                    if(Long.toString(l).equals(id1))
                    {
                        tweet1=t.text;
                        
                    }
                    
                }
                for(Tweets t:arraylist)
                {
                    long l=t.id;
                    if(Long.toString(l).equals(id2))
                    {
                        tweet2=t.text;
                    }
                }
                List<String> a = Arrays.asList(tweet1.toLowerCase().split(" "));
                List<String> b = Arrays.asList(tweet2.toLowerCase().split(" "));
                Set<String> a1 = new HashSet<>(a);
                a1.addAll(b);
                Set<String> intersection = new HashSet<>(a);
                intersection.retainAll(b);
                distanceMatrix[i][j]=Double.toString(1 - (intersection.size()/ (double)a1.size()));
                if(Double.parseDouble(distanceMatrix[i][j])<min)
                {
                   min=Double.parseDouble(distanceMatrix[i][j]);
                   id=Long.parseLong(distanceMatrix[0][j]);
                }
                
            }
            squaredError+=min*min;
            //System.out.println(squaredError);
            sum=sum+squaredError;
            //System.out.println(sum);
            distanceMatrix[i][Integer.parseInt(noOfClusters1)+1]=Long.toString(id);
        }
        int len=0;
        for(int j=1;j<=Integer.parseInt(noOfClusters1);j++)
        {
            ArrayList<Long> a5=new ArrayList<>();
            for(int i=1;i<lineCount+1;i++)
            {
             if(distanceMatrix[i][Integer.parseInt(noOfClusters1)+1].equals(distanceMatrix[0][j]))
             {
              a5.add(Long.parseLong(distanceMatrix[i][0]));
             }
            }
            len=len+a5.size();
            arrayList.add(a5);
           
        }

        /*for(int i=0;i<lineCount+1;i++)
        {
            for(int j=0;j<Integer.parseInt(noOfClusters1)+2;j++)
            {
                System.out.print(distanceMatrix[i][j]+"   ");
            }
            System.out.println();
        }*/
        int hp=0,t=0;
        if(v==24)
        {
        for(ArrayList<Long> a5:arrayList)
        {
            out.write("Cluster id: "+(t+1)+" "+a5+"\r\n");
            out.flush();
            out.write("\r\n");
            t++;
            flag=1;
            
        }
        out.write("SSE:"+sum);
        out.flush();
        }
        for(ArrayList<Long> a5:arrayList)
        {
            double minDistance=Double.POSITIVE_INFINITY;
            ArrayList<DoubleLong> er=new ArrayList<>();
            long id1=0;
            //System.out.println(a5.size());
            for(long l:a5)
            {
               ArrayList<Tweets> ar=new ArrayList<>();
               
               long long1=0;
               Tweets twe = null ;
               for(Tweets e:arraylist)
               {
                  if(e.id==l)
                  {
                      String tweet1=e.text;
                      twe=new Tweets(tweet1,l);
                      long1=l;
                  }
                  else
                  {
                      Tweets c=new Tweets(e.text,e.id);
                      ar.add(c);
                  }
               }
               double JaccardDistance=CalculateMinJaccardDistance(twe,ar);
               //arrayp[u++]=minJaccardDistance
               DoubleLong dl=new DoubleLong(JaccardDistance,long1);
               er.add(dl);
            }
            //System.out.println(er.size());
            double mi=Double.MAX_VALUE;
             for(DoubleLong d1:er)
              {
                  if(d1.JaccardDistance<mi)
                  {
                   
                      mi=d1.JaccardDistance;
                      id1=d1.id;
                  }
              }
              distanceMatrix[0][hp+1]=Long.toString(id1);
              abcd.add(id1);
              hp++;

        }        
        }
        int d=0;
        if(flag==0)
        {
        for(ArrayList<Long> a5:arrayList)
        {
            out.write("Cluster id: "+(d+1)+" "+a5+"\r\n");
            out.flush();
            out.write("\r\n");
            d++;
            
        }
        out.write("SSE:"+sum+"\r\n");
        out.flush();
        }
        
    }

    private static double CalculateMinJaccardDistance(Tweets twe, ArrayList<Tweets> ar) {
        double min = 0;
        for(Tweets t:ar)
        {
            List<String> a = Arrays.asList(t.text.toLowerCase().split(" "));
                List<String> b = Arrays.asList(twe.text.toLowerCase().split(" "));
                Set<String> a1 = new HashSet<>(a);
                a1.addAll(b);
                Set<String> intersection = new HashSet<>(a);
                intersection.retainAll(b);
                double ans=(1 - (intersection.size() / (double)a1.size()));
                min=min+ans;
        }
        return min;
    }
   private static class Tweets {
    String text;
    long id;
    Tweets(String a,long b)
    {
        text=a;
        id=b;
    }
    String getText()
    {
        return text;
    }
    long getId()
    {
        return id;
    }
}

    private static class DoubleLong {
       double JaccardDistance;
       long id;
        public DoubleLong(double a,long b) {
            JaccardDistance=a;
            id=b;
        }
    }

  
 
}
