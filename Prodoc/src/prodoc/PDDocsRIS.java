/*
 * OpenProdoc
 * 
 * See the help doc files distributed with
 * this work for additional information regarding copyright ownership.
 * Joaquin Hierro licenses this file to You under:
 * 
 * License GNU GPL v3 http://www.gnu.org/licenses/gpl.html
 * 
 * you may not use this file except in compliance with the License.  
 * Unless agreed to in writing, software is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * author: Joaquin Hierro      2015
 * 
 */

package prodoc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

/**
 *
 * @author Joaquin
 */
public class PDDocsRIS extends PDDocs
{
static private TreeSet<String> TagList=null;
static private final String START_REC="TY  -";
static private final String END_REC="ER  -";
static private final String URL_REC="UR";
static private final int TAG_LENGTH=5;

//-------------------------------------------------------------------------    
public PDDocsRIS(DriverGeneric Drv, String pDocType) throws PDException
{
super(Drv, pDocType);
}
public int ImportFileRIS(String ActFolderId, String RISFilepath) throws PDException
{
int NumDocs=0;
BufferedReader Metadata=null;
TreeSet<String> LT=getTagList();
try {
Metadata = new BufferedReader(new FileReader(RISFilepath));
String DocMeta=Metadata.readLine();
String CurVal="";
String CurField="";
PDDocs Doc=null;
Record R=null;
Attribute Attr;
while (DocMeta!=null)
    {
    DocMeta=DocMeta.trim();
    if (!LT.contains(DocMeta.substring(0, 5)))
        CurVal+=DocMeta;
    else
        {
        if (DocMeta.substring(0, TAG_LENGTH).equalsIgnoreCase(START_REC))
            {
            Doc=new PDDocs(getDrv(), getDocType());
            R=Doc.getRecSum();
            Attribute Attr1=R.getAttr("RIS_"+START_REC.substring(0, 2));
            String Val=DocMeta.substring(TAG_LENGTH).trim();
            R.getAttr("RIS_"+START_REC.substring(0, 2)).setValue(DocMeta.substring(TAG_LENGTH).trim());
            }
        else if (DocMeta.substring(0, TAG_LENGTH).equalsIgnoreCase(END_REC))
            {
            if (Doc!=null)
                {
                if (CurField.length()!=0 && CurVal.length()!=0)
                   R.getAttr(CurField).setValue(CurVal); 
                Doc.assignValues(R);
                Doc.setParentId(ActFolderId);
                if (Doc.getUrl()==null || Doc.getUrl().length()==0)
                    Doc.setName("http://");
                Doc.insert();
                }
            }
        else 
            {
            if (CurField.length()!=0 && CurVal.length()!=0)
                {
                if (!R.getAttr(CurField).isMultivalued())
                    R.getAttr(CurField).setValue(CurVal); 
                else
                    R.getAttr(CurField).AddValue(CurVal);
                }
            CurField="RIS_"+DocMeta.substring(0, 2);
            if (CurField.equalsIgnoreCase(URL_REC))
               CurField=PDDocs.fNAME;
            CurVal=DocMeta.substring(TAG_LENGTH).trim();
            }
        }
    DocMeta=Metadata.readLine();
    }
Metadata.close();
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    if (Metadata!=null)
        try {
            Metadata.close();
        } catch (IOException ex1) 
            {
            }
    throw new PDException(ex.getLocalizedMessage());
    }
return(NumDocs);
}
//-------------------------------------------------------------------------    
private TreeSet<String> getTagList()
{
if (TagList==null)
    {
    TagList=new TreeSet();
    TagList.add("TY  -");
    TagList.add("A2  -");
    TagList.add("A3  -");
    TagList.add("A4  -");
    TagList.add("AB  -");
    TagList.add("AD  -");
    TagList.add("AN  -");
    TagList.add("AU  -");
    TagList.add("C1  -");
    TagList.add("C2  -");
    TagList.add("C3  -");
    TagList.add("C4  -");
    TagList.add("C5  -");
    TagList.add("C6  -");
    TagList.add("C7  -");
    TagList.add("C8  -");
    TagList.add("CA  -");
    TagList.add("CN  -");
    TagList.add("CY  -");
    TagList.add("DA  -");
    TagList.add("DB  -");
    TagList.add("DO  -");
    TagList.add("DP  -");
    TagList.add("EP  -");
    TagList.add("ET  -");
    TagList.add("IS  -");
    TagList.add("J2  -");
    TagList.add("KW  -");
    TagList.add("L1  -");
    TagList.add("L4  -");
    TagList.add("LA  -");
    TagList.add("LB  -");
    TagList.add("M1  -");
    TagList.add("M3  -");
    TagList.add("N1  -");
    TagList.add("NV  -");
    TagList.add("OP  -");
    TagList.add("PB  -");
    TagList.add("PY  -");
    TagList.add("RI  -");
    TagList.add("RN  -");
    TagList.add("RP  -");
    TagList.add("SE  -");
    TagList.add("SN  -");
    TagList.add("SP  -");
    TagList.add("ST  -");
    TagList.add("T1  -");
    TagList.add("T2  -");
    TagList.add("T3  -");
    TagList.add("TA  -");
    TagList.add("TI  -");
    TagList.add("TT  -");
    TagList.add("UR  -");
    TagList.add("VL  -");
    TagList.add("Y2  -");
    TagList.add("ER  -");
    }
return(TagList);
}
//-------------------------------------------------------------------------    
}
