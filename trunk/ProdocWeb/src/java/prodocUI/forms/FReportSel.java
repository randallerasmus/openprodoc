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

package prodocUI.forms;


import html.*;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import prodoc.Attribute;
import prodoc.Cursor;
import prodoc.DriverGeneric;
import prodoc.PDDocs;
import prodoc.PDException;
import prodoc.PDFolders;
import prodoc.PDReport;
import prodoc.Record;
import prodocUI.servlet.SParent;
import prodocUI.servlet.SendDoc;

/**
 *
 * @author jhierrot
 */
public class FReportSel extends FFormBase
{
final static public String NumDocsPageName="NUMDOCSPAGE";
final static public String NumPagesFileName="NUMPAGESFILE";
public FieldText NumDocsPage;
public FieldText NumPagesFile;

/** Creates a new instance of FormularioLogin
 * @param Req
 * @param pMode
 * @param pRec
 * @param Destination
 * @throws PDException
 */
public FReportSel(HttpServletRequest Req, int pMode, Record pRec, String Destination) throws PDException
{
super(Req, SParent.TT(Req, "Reports_Generation"), pMode, pRec);
DriverGeneric PDSession=SParent.getSessOPD(Req);
AddCSS("prodoc.css");
AddJS("Types.js");
Table BorderTab=new Table(1, 3, 0);
BorderTab.setCSSClass("FFormularios");
BorderTab.setAlineacion(Table.CENTER);
BorderTab.setWidth(-100);
BorderTab.getCelda(0,0).AddElem(new Element(TT("Reports_Generation")));
BorderTab.getCelda(0,0).setCSSClass("FTitle");
BorderTab.getCelda(0,2).AddElem(Status);
BorderTab.getCelda(0,2).AddElem(Element.getEspacio2());
BorderTab.getCelda(0,2).AddElem(HHelp);
BorderTab.setContorno(true);
Table FormTab=new Table(3, 4, 0);
FormTab.setCellPadding(10);
FormTab.setWidth(-100);
FormTab.setCSSClass("FFormularios");
NumDocsPage=new FieldText(NumDocsPageName);
NumDocsPage.setMaxSize(6);
NumDocsPage.setCSSClass("FFormInput");
NumDocsPage.setMensStatus(TT("Docs_per_Page"));
NumPagesFile=new FieldText(NumPagesFileName);
NumPagesFile.setMaxSize(6);
NumPagesFile.setCSSClass("FFormInput");
NumPagesFile.setMensStatus(TT("Pages_per_File"));
//FormTab.getCelda(0,0).setWidth(-25);
FormTab.getCelda(0,0).setHeight(30);
FormTab.getCelda(1,0).AddElem(new Element(TT("Docs_per_Page")+":"));
FormTab.getCelda(1,0).AddElem(Element.getEspacio2());
FormTab.getCelda(1,0).AddElem(NumDocsPage);
FormTab.getCelda(1,1).AddElem(new Element(TT("Pages_per_File")+":"));
FormTab.getCelda(1,1).AddElem(Element.getEspacio2());
FormTab.getCelda(1,1).AddElem(NumPagesFile);
Table TabDocs=new Table(5,1,1);
TabDocs.setCellSpacing(0);
TabDocs.setWidth(-100);
TabDocs.setHeight(-100);
TabDocs.getFila(0).setCSSClass("ListDocsHead");
PDReport Rep=new PDReport(PDSession);
Record NextDoc=Rep.getRecSum();
Attribute AttrD=NextDoc.getAttr(PDReport.fTITLE);
TabDocs.getCelda(0,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fDOCSPAGE);
TabDocs.getCelda(1,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fPAGESDOC);
TabDocs.getCelda(2,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(PDReport.fMIMETYPE);
TabDocs.getCelda(3,0).AddElem(new Element(TT(AttrD.getUserName())));
AttrD=NextDoc.getAttr(Rep.fPDDATE);
TabDocs.getCelda(4,0).AddElem(new Element(TT(AttrD.getUserName())));
String ActDoc=SParent.getActDocId(Req);
String DocId=null;
Cursor ListDocs=Rep.GetListReports();
int Row=0;
NextDoc=PDSession.NextRec(ListDocs);
while (NextDoc!=null)
    {
    TabDocs.AddFila(); Row++;
    AttrD=NextDoc.getAttr(PDReport.fTITLE);
    TabDocs.getCelda(0,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDReport.fDOCSPAGE);
    TabDocs.getCelda(1,Row).AddElem(new Element(((Integer)AttrD.getValue()).toString()));
    AttrD=NextDoc.getAttr(PDReport.fPAGESDOC);
    TabDocs.getCelda(2,Row).AddElem(new Element(((Integer)AttrD.getValue()).toString()));
    AttrD=NextDoc.getAttr(PDReport.fMIMETYPE);
    TabDocs.getCelda(3,Row).AddElem(new Element((String)AttrD.getValue()));
    AttrD=NextDoc.getAttr(PDReport.fPDDATE);
    TabDocs.getCelda(4,Row).AddElem(new Element(SParent.FormatTS(Req,(Date)AttrD.getValue())));
    DocId=(String)NextDoc.getAttr(PDDocs.fPDID).getValue();
    if (DocId.equals(ActDoc))
        TabDocs.getFila(Row).setCSSClass("ListDocsSel");
    else
        TabDocs.getFila(Row).setCSSClass("ListDocs");
    TabDocs.getFila(Row).setCSSId(DocId);
    TabDocs.getFila(Row).setOnClick("SelectRow('"+DocId+"')");
    NextDoc=PDSession.NextRec(ListDocs);
    }
FormTab.getCelda(1,2).setCSSClass("ListDocs");
FormTab.getCelda(1,2).AddElem(TabDocs);
PDSession.CloseCursor(ListDocs);
FormTab.getCelda(1,3).AddElem(OkButton);
FormTab.getCelda(1,3).AddElem(CancelButton);
Form LoginForm=new Form(Destination+"?Read=1","FormVal");
BorderTab.getCelda(0,1).AddElem(FormTab);
LoginForm.AddElem(BorderTab);
AddElem(LoginForm);
}
//-----------------------------------------------------------------------------------------------    
@Override
protected String getFormHelp()
{
switch (Mode)  
    {
    case ADDMOD:
        return("AddFolder");
    case DELMOD:
        return("DelFolder");
    case EDIMOD:
        return("ModFolder");
    }
return("HelpIndex");
}
//-----------------------------------------------------------------------------------------------    
}
