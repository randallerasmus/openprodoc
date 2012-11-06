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
 * author: Joaquin Hierro      2012
 * 
 */

package prodoc;

import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class Thesaur for managong the thesaurus in Database
 * @author jhierrot
 */
public class PDThesaur extends ObjPD
{
/**
 *
 */
public static final String fPDID="PDId";
/**
 *
 */
public static final String fNAME="Name";
public static final String fDESCRIP="Description";
public static final String fUSE="USE";
public static final String fPDID2="PDId2";

public static final String ROOTTERM="ROOT";
/**
 *
 */
public static final String fPARENTID="ParentId";
/**
 *
 */
public static final String fGRANTPARENTID="GrantParentId";
/**
 *
 */
static private Record ThesaurStruct=null;
/**
 *
 */
static private Record ThesaurLevStruct=null;
static private Record ThesaurRTStruct=null;
/**
 *
 */
private String PDId=null;
/**
 *
 */
private String Name=null;
private String Description=null;
private String Use=null;
/**
 *
 */
private String ParentId=null;
/**
 *
 */
private boolean IsRootThesaur=false;
/**
 *
 */
private Record RecSum=null;

static private ObjectsCache ThesaurObjectsCache = null;

//-------------------------------------------------------------------------
/**
 *
 * @param Drv
 * @throws PDException
 */
public PDThesaur(DriverGeneric Drv) throws PDException
{
super(Drv);
}
//-------------------------------------------------------------------------
@Override
public void assignValues(Record Rec) throws PDException
{
setPDId((String) Rec.getAttr(fPDID).getValue());
setName((String) Rec.getAttr(fNAME).getValue());
setDescription((String) Rec.getAttr(fDESCRIP).getValue());
setUse((String) Rec.getAttr(fUSE).getValue());
setParentId((String) Rec.getAttr(fPARENTID).getValue());
assignCommonValues(Rec);
}
//-------------------------------------------------------------------------
/**
* @return the PDId
*/
public String getPDId()
{
return PDId;
}
//-------------------------------------------------------------------------
/**
 * @param pPDId
 */
public void setPDId(String pPDId) throws PDExceptionFunc
{
this.PDId = pPDId;
}
//-------------------------------------------------------------------------
/**
* @return the Title
*/
public String getName()
{
return Name;
}
//-------------------------------------------------------------------------
/**
 * @param pName
 * @throws PDExceptionFunc  
*/
public void setName(String pName) throws PDExceptionFunc
{
if (pName==null)    
    return;
pName=pName.trim();
if (pName.length()==0)   
    PDExceptionFunc.GenPDException("Empty_Name_not_allowed",pName);
if (pName.length()>128)   
    PDExceptionFunc.GenPDException("Name_longer_than_allowed",pName);
this.Name = pName;

}
//-------------------------------------------------------------------------
/**
 * object "method" needed because static overloading doesn't work in java
 * @return
 */
@Override
public String getTabName()
{
return(getTableName());
}
//-------------------------------------------------------------------------
/**
 * static equivalent method
 * @return
 */
static public String getTableName()
{
return("PD_THESAUR");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameThesLev()
{
return("PD_THES_LEV");
}
//-------------------------------------------------------------------------
/**
 * static method
 * @return
 */
static public String getTableNameThesRT()
{
return("PD_THES_RT");
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
@Override
synchronized public Record getRecord() throws PDException
{
Record Rec=getRecordStruct();    
Rec.getAttr(fPDID).setValue(getPDId());
Rec.getAttr(fNAME).setValue(getName());
Rec.getAttr(fPDDATE).setValue(getPDDate());
Rec.getAttr(fPARENTID).setValue(getParentId());
getCommonValues(Rec);
return Rec;

}
//-------------------------------------------------------------------------
@Override
protected Record getRecordStruct() throws PDException
{
if (ThesaurStruct==null)
    ThesaurStruct=CreateRecordStructPDThesaur();
return( ThesaurStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static public Record getRecordStructPDThesaur() throws PDException
{
if (ThesaurStruct==null)
    ThesaurStruct=CreateRecordStructPDThesaur();
return(ThesaurStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaur() throws PDException
{
if (ThesaurStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID, "PDID","Unique_identifier", Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fNAME, "Term_Name","Term_Name", Attribute.tSTRING, true, null, 128, false, true, true));
    R.addAttr( new Attribute(fDESCRIP, "Description","Description", Attribute.tSTRING, true, null, 254, false, false, true));
    R.addAttr( new Attribute(fUSE, "USE","Use_this_term", Attribute.tSTRING, false, null, 32, false, false, true));
    R.addAttr( new Attribute(fPARENTID, "Parent_Term","Parent_Term", Attribute.tSTRING, true, null, 32, false, true, false));
    R.addRecord(getRecordStructCommon());
    return(R);
    }
else
    return(ThesaurStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDThesaurLev() throws PDException
{
if (ThesaurLevStruct==null)
    ThesaurLevStruct=CreateRecordStructPDThesaurLev();
return(ThesaurLevStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaurLev() throws PDException
{
if (ThesaurLevStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fGRANTPARENTID,fGRANTPARENTID, fGRANTPARENTID, Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID, fPDID, fPDID, Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(ThesaurLevStruct);
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static protected Record getRecordStructPDThesaurRT() throws PDException
{
if (ThesaurRTStruct==null)
    ThesaurRTStruct=CreateRecordStructPDThesaurRT();
return(ThesaurRTStruct.Copy());
}
//-------------------------------------------------------------------------
/**
 * Returns the fixed structure for levels of Thesaurs
 * @return
 * @throws PDException
 */
static private synchronized Record CreateRecordStructPDThesaurRT() throws PDException
{
if (ThesaurRTStruct==null)
    {
    Record R=new Record();
    R.addAttr( new Attribute(fPDID2,fPDID2, fPDID2, Attribute.tSTRING, true, null, 32, true, false, false));
    R.addAttr( new Attribute(fPDID, fPDID, fPDID, Attribute.tSTRING, true, null, 32, true, false, false));
    return(R);
    }
else
    return(ThesaurRTStruct);
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
protected Conditions getConditions() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 * Builds the default conditions identifying the Thesaur: Id="id"
 * @return List of conditions
 * @throws PDException
 */
protected Conditions getConditionsMaint() throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fPDID, Condition.cEQUAL, getPDId()));
return(ListCond);
}
//-------------------------------------------------------------------------
protected Conditions getConditionsLike(String Name) throws PDException
{
Conditions ListCond=new Conditions();
ListCond.addCondition(new Condition(fNAME, Condition.cLIKE, VerifyWildCards(Name)));
return(ListCond);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 */
protected void AsignKey(String Ident) throws PDExceptionFunc
{
setPDId(Ident);
}
//-------------------------------------------------------------------------
/**
* The install method is generic because for instantiate a object, the class
* need to access to the tables for definition
 * @param Drv
 * @throws PDException
*/
static protected void InstallMulti(DriverGeneric Drv) throws PDException
{
Drv.AddIntegrity(getTableName(), fPARENTID, getTableName(),           fPDID);
Drv.CreateTable(getTableNameThesLev(), getRecordStructPDThesaurLev());
Drv.AddIntegrity(getTableNameThesLev(), fPDID,          getTableName(), fPDID);
Drv.AddIntegrity(getTableNameThesLev(), fGRANTPARENTID, getTableName(), fPDID);
Drv.CreateTable(getTableNameThesRT(), getRecordStructPDThesaurRT());
Drv.AddIntegrity(getTableNameThesRT(), fPDID,          getTableName(), fPDID);
Drv.AddIntegrity(getTableNameThesRT(), fPDID2, getTableName(), fPDID);
}
//-------------------------------------------------------------------------
/**
 * creates a new term of the Thesaurus
 * @throws PDException
 */
@Override
public void insert() throws PDException
{
boolean InTransLocal;
VerifyAllowedIns();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
if (getPDId()==null || getPDId().length()==0)
    setPDId(GenerateId());
else if (!IsRootThesaur && getParentId().equalsIgnoreCase(getPDId()))
       PDException.GenPDException("Parent_Term_equals_to_actual_Term", getParentId());
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.insert:"+getPDId());
AddLogFields();
getRecord().Verify();
getDrv().InsertRecord(getTabName(), getRecord());
if (!IsRootThesaur)
    ActFoldLev();
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_creating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Insert<");
}
//-------------------------------------------------------------------------
protected void VerifyAllowedIns() throws PDException
{
if (!getDrv().getUser().getRol().isAllowCreateThesaur() )
   PDExceptionFunc.GenPDException("Thesaur_creation_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedDel() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainThesaur() )
    PDExceptionFunc.GenPDException("Thesaur_delete_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
protected void VerifyAllowedUpd() throws PDException
{
if (!getDrv().getUser().getRol().isAllowMaintainThesaur() )
   PDExceptionFunc.GenPDException("Thesaur_update_not_allowed_to_user", null);
}
//-------------------------------------------------------------------------
/**
* @return the ParentId
*/
public String getParentId()
{
return ParentId;
}
//-------------------------------------------------------------------------
/**
 * @param pParentId
 */
public void setParentId(String pParentId)
{
this.ParentId = pParentId;
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
public void CreateRootThesaur() throws PDException
{
IsRootThesaur=true;
insert();
IsRootThesaur=false;
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
public String GenerateId()
{
StringBuilder genId = new StringBuilder(32);
genId.append(Long.toHexString(System.currentTimeMillis()));
genId.append("-");
genId.append(Long.toHexString(Double.doubleToLongBits(Math.random())));
return genId.toString();
}
//-------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
private void ActFoldLev() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ActFoldLev");
HashSet GrandParentList=getListGrandParentList(getParentId());
GrandParentList.add(getParentId());
Record RecFL=getRecordStructPDThesaurLev();
RecFL.getAttr(fPDID).setValue(getPDId());
for (Iterator it = GrandParentList.iterator(); it.hasNext();)
    {
    Object GP = it.next();
    RecFL.getAttr(fGRANTPARENTID).setValue((String)GP);
    getDrv().InsertRecord(getTableNameThesLev(), RecFL);
    }
}
//-------------------------------------------------------------------------
/**
 *
 * @param Parent
 * @return a Set containing the parents of Parent
 * @throws PDException
 */
public HashSet getListGrandParentList(String Parent) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListGrandParentList>:"+Parent);
HashSet Result=new HashSet(4);
Condition CondParents=new Condition( fPDID, Condition.cEQUAL, Parent);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameThesLev(), getRecordStructPDThesaurLev(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fGRANTPARENTID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListGrandParentList<:"+Parent);
return(Result);
}
//-------------------------------------------------------------------------
/**
 *
 * @param PDId
 * @return a Set containing the parents of Parent
 * @throws PDException
 */
public HashSet getListDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDescendList>:"+PDId);
HashSet Result=new HashSet();
Condition CondParents=new Condition( fGRANTPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTableNameThesLev(), getRecordStructPDThesaurLev(), Conds);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fPDID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 * Deletes the Thesaur and all the documents and subThesaurs contained recursively
 * If the number of Thesaurs, documents and levels is too big, this method can 
 * create problems in the rollback of the database storing the metadata
 * @throws PDException in any problem and cancels the Transaction. When enough permissions throws PDFuncException.
 */
@Override
public void delete() throws PDException
{
if (PDLog.isInfo())
    PDLog.Debug("PDThesaurs.delete>:"+getPDId());
boolean InTransLocal;
Load(getPDId());
VerifyAllowedDel();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
DeleteTermRT();    
DeleteTermsInTerm();
DeleteFoldLevelParents();
getDrv().DeleteRecord(getTabName(), getConditions());
getObjCache().remove(getKey());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_deleting_Thesaur",Ex.getLocalizedMessage());
    throw Ex;
    }
if (InTransLocal)
    getDrv().CerrarTrans();
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.delete<:"+getPDId());
}
//-------------------------------------------------------------------------
private void DeleteTermRT() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteTermRT:"+getPDId());     
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(PDThesaur.fPDID, Condition.cEQUAL, getPDId()));
Conds.addCondition(new Condition(PDThesaur.fPDID2, Condition.cEQUAL, getPDId()));
Conds.setOperatorAnd(false);
getDrv().DeleteRecord(getTableNameThesRT(), Conds);
}
//-------------------------------------------------------------------------
/**
 * Recursive Deletes all the Thesaurs in actual Thesaur. 
 * Used as part of Thesaur delete
 */
private void DeleteTermsInTerm() throws PDException
{   
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteTermsInTerm:"+getPDId());     
Conditions Conds=new Conditions();
Condition CondChilds=new Condition(PDThesaur.fPARENTID, Condition.cEQUAL, getPDId());
Conds.addCondition(CondChilds);
Query Q=new Query(PDThesaur.getTableName(), PDThesaur.getRecordStructPDThesaur(), Conds);   
Cursor ListThesaursContained=getDrv().OpenCursor(Q);
Record NextFold=getDrv().NextRec(ListThesaursContained);
PDThesaur Fold2del=new PDThesaur(getDrv());
while (NextFold!=null)
    {
    Fold2del.assignValues(NextFold);  
    Fold2del.delete();
    NextFold=getDrv().NextRec(ListThesaursContained);
    }
getDrv().CloseCursor(ListThesaursContained);
}
//-------------------------------------------------------------------------
/**
 * Deletes all the references in upper levels to this Thesaur
 * @throws PDException
 */
private void DeleteFoldLevelParents()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.DeleteFoldLevelParents:"+getPDId());    
Condition CondChilds=new Condition( fPDID, Condition.cEQUAL, getPDId());
Conditions Conds=new Conditions();
Conds.addCondition(CondChilds);
getDrv().DeleteRecord(getTableNameThesLev(), Conds);
}
//-------------------------------------------------------------------------
/**
 * Updates the metadata of the Thesaur
 * @throws PDException
 */
@Override
public void update()  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.update:"+getPDId());
boolean InTransLocal;
VerifyAllowedUpd();
InTransLocal=!getDrv().isInTransaction();
if (InTransLocal)
    getDrv().IniciarTrans();
try {
AddLogFields();
getDrv().UpdateRecord(getTabName(), getRecord(), getConditions());
getObjCache().put(getKey(), getRecord());
} catch (PDException Ex)
    {
    getDrv().AnularTrans();
    PDException.GenPDException("Error_updating_Thesaur",Ex.getLocalizedMessage());
    }
if (InTransLocal)
    getDrv().CerrarTrans();
}
//-------------------------------------------------------------------------
/**
 * Return a list of all the Thesaurs of any type whose DIRECT parent is PDId
 * @param PDId Id of the parent Thesaur
 * @return a hashset with all the DirectDescend
 * @throws PDException in any error
 */
public HashSet getListDirectDescendList(String PDId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDirectDescendList>:"+PDId);
LinkedHashSet Result=new LinkedHashSet(5);
Condition CondParents=new Condition( fPARENTID, Condition.cEQUAL, PDId);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fPDID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListDirectDescendList<:"+PDId);
return(Result);
}
//-------------------------------------------------------------------------
/**
 *
 * @param Ident
 * @return
 * @throws PDException
 */
public Record Load(String Ident)  throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Load:"+Ident);
AsignKey(Ident);
Record r=(Record)getObjCache().get(Ident);
if (r==null) 
    {
    Query LoadAct=new Query(getTabName(), getRecordStructPDThesaur(),getConditions());
    Cursor Cur=getDrv().OpenCursor(LoadAct);
    r=getDrv().NextRec(Cur);
    getDrv().CloseCursor(Cur);
    getObjCache().put(Ident, r);
    }
if (r!=null)
    assignValues(r);
return(r);
}
//-------------------------------------------------------------------------
/**
 *
 * @return
 */
protected String getDefaultOrder()
{
return(fNAME);
}
//-------------------------------------------------------------------------
public String toString()
{
return this.getName();
}
//-------------------------------------------------------------------------
/**
 * Returns the Id of a Term with name TermName child of Term ParentId
 * @param ParentId id of parent Term
 * @param TermName name of child Term
 * @return Id of the found Term
 * @throws PDException if the Thesaur dosen't exist or the user it'snt allowed
 */
public String GetIdChild(String ParentId, String TermName) throws PDException
{
Condition CT=new Condition(fNAME, Condition.cEQUAL, TermName);
Condition CP=new Condition(fPARENTID, Condition.cEQUAL, ParentId);
Conditions Conds=new Conditions();
Conds.addCondition(CT);
Conds.addCondition(CP);
Query LoadAct=new Query(getTabName(), getRecordStructPDThesaur(), Conds);
Cursor Cur=getDrv().OpenCursor(LoadAct);
Record r=getDrv().NextRec(Cur);
getDrv().CloseCursor(Cur);
if (r==null)
    PDExceptionFunc.GenPDException("do_not_exist_Term_under_Term", ParentId+"/"+TermName);
Attribute A=r.getAttr(fPDID);
return((String)A.getValue());
}
//-------------------------------------------------------------------------
/**
 * Search for Thesaurs returning a cursor with the results of Thesaurs with the
 * indicated values of fields. Only return the Thesaurs alowed for the user, as defined by ACL.
 * @param ThesaurType Type of Thesaur to search. Can return Thesaurs of subtype.
 * @param AttrConds Conditions over the fields ofthe ThesaurType
 * @param SubTypes if true, returns results of the indicated type AND susbtipes
 * @param SubThesaurs if true seach in actual Thesaur AND subThesaurs, if false, serach in ALL the structure
 * @param IdActFold Thesaur to start the search. if null, start in the root level
 * @param Ord
 * @return a Cursor with the results of the query to use o send to NextTerm()
 * @throws PDException when occurs any problem
 */
public Cursor Search(String ThesaurType, Conditions AttrConds, boolean SubThesaurs, String IdActFold, Vector Ord) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Search >:"+ThesaurType+" {"+AttrConds+"} SubThesaurs:"+SubThesaurs+" IdActFold:"+IdActFold+" Ord:"+Ord);
PDThesaur F=new PDThesaur(getDrv());
Conditions ComposedConds=new Conditions();
ComposedConds.addCondition(AttrConds);
Vector TypList=new Vector();
TypList.add(ThesaurType);
if (SubThesaurs)
    {
    if (!(IdActFold==null) )
        {
        Condition C=new Condition(PDThesaur.fPDID, F.getListDescendList(IdActFold));
        ComposedConds.addCondition(C);
        }
    }
Record RecSearch=F.getRecord().CopyMono();
if (RecSearch.ContainsAttr(fPDID))
    {
    RecSearch.getAttr(fPDID).setName((String)TypList.get(0)+"."+fPDID);
    }
else
    {
    Attribute Atr=getRecord().getAttr(fPDID).Copy();
    Atr.setName((String)TypList.get(0)+"."+fPDID);
    RecSearch.addAttr(Atr);
    }
Query FoldSearch=new Query(TypList, RecSearch, ComposedConds, Ord);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.Search <");
return(getDrv().OpenCursor(FoldSearch));
}
//-------------------------------------------------------------------------
/**
 * Receives a cursor created with method Search and returns the next Term or null if
 * there are no more.
 * @param Res Cursor created with method Search and serevral parameters
 * @return A new Object created with the data of the next row
 * @throws PDException when occurs any problem
 */
public PDThesaur NextTerm(Cursor Res) throws PDException
{
PDThesaur NextF=null;
Record Rec=getDrv().NextRec(Res);
if (Rec==null)
    return(NextF);
NextF.assignValues(Rec);
return(NextF);
}
//-------------------------------------------------------------------------
/**
 * Create if necesary and Assign the Cache for the objects of this type of object
 * @return the cache object for the type
 */
protected ObjectsCache getObjCache()
{
if (ThesaurObjectsCache==null)
    ThesaurObjectsCache=new ObjectsCache("Thes");
return(ThesaurObjectsCache);    
}
//-------------------------------------------------------------------------
/**
 * Returns the value/field used as key of the object (Id) to ve used in Cache index
 * @return the value of the  Id field
 */
@Override
protected String getKey()
{
return(getPDId());
}
//-------------------------------------------------------------------------
/**
 * Add aditional information, oriented a "extended" object with childrn nodes
 * @return The aditional XML
 * @throws PDException
 */
@Override
protected String toXML2() throws PDException
{
Record RFull=getRecord().Copy();
RFull.delRecord(getRecord());
return(RFull.toXML()+"</ListAttr>");    
}
//-------------------------------------------------------------------------
public PDThesaur ImportXMLNode(Node OPDObject, String ParentThesaurId, boolean MaintainId) throws PDException
{
NodeList childNodes = OPDObject.getChildNodes();
PDThesaur NewFold=null;
for (int i = 0; i < childNodes.getLength(); i++)
    {
    Node item = childNodes.item(i);
    if (item.getNodeName().equalsIgnoreCase(XML_ListAttr)) 
        {
        Record r=Record.FillFromXML(item, getRecord());
        NewFold=new PDThesaur(getDrv()); 
        NewFold.assignValues(r);
        if (!MaintainId)
            NewFold.setPDId(null);
        NewFold.setParentId(ParentThesaurId);
        }
    }
NewFold.insert();
return NewFold;
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLFile
 * @param ParentThesaurId
 * @return 
 * @throws PDException
 */
public PDThesaur ProcessXML(File XMLFile, String ParentThesaurId) throws PDException
{
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
PDThesaur NewFold=null;
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    NewFold=ImportXMLNode(OPDObject, ParentThesaurId, false);
    }
return(NewFold); // returned LAST Thesaur when opd file contains several.
}catch(Exception ex)
    {
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
/**
* @return the Description
*/
public String getDescription()
{
return Description;
}
//---------------------------------------------------------------------
/**
 * @param pDescription
 * @throws PDExceptionFunc  
 */
public void setDescription(String pDescription) throws PDExceptionFunc
{
if (pDescription.length()>254)   
    PDExceptionFunc.GenPDException("Description_longer_than_allowed", pDescription);
Description = pDescription;
}
//---------------------------------------------------------------------
/**
* @return the Use
*/
public String getUse()
{
return Use;
}
//---------------------------------------------------------------------
/**
* @param Use the Use to set
*/
public void setUse(String Use)
{
this.Use = Use;
}
//---------------------------------------------------------------------
/**
 * List all the Thesaurs
 * @return hashset containing all the thesaurs Id
 * @throws PDExceptionon any error
 */
public HashSet ListThes() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListThes>:"+PDId);
LinkedHashSet Result=new LinkedHashSet(5);
Condition CondParents=new Condition(fPARENTID, fPDID);
Conditions Conds=new Conditions();
Conds.addCondition(CondParents);
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
Record Res=getDrv().NextRec(CursorId);
while (Res!=null)
    {
    String Acl=(String) Res.getAttr(fPDID).getValue();
    Result.add(Acl);
    Res=getDrv().NextRec(CursorId);
    }
getDrv().CloseCursor(CursorId);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListThes<:"+PDId);
return(Result);
}
//---------------------------------------------------------------------
public HashSet getListRT(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListRT>:"+PDId);
Condition CondRT1=new Condition(fPDID, Condition.cEQUAL, TermId);
Condition CondRT2=new Condition(fPDID2, Condition.cEQUAL, TermId);
Conditions Conds=new Conditions();
Conds.setOperatorAnd(false);
Conds.addCondition(CondRT1);
Conds.addCondition(CondRT2);
Query Q=new Query(getTableNameThesRT(), getRecordStructPDThesaurRT(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListRT<:"+PDId);
return(CursorId);
}
//---------------------------------------------------------------------
public Cursor ListRT(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT>:"+PDId);
Condition CondRT1=new Condition(fPDID, getListRT(TermId));
Conditions Conds=new Conditions();
Conds.addCondition(CondRT1);
Query Q=new Query(getTableName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListRT<:"+PDId);
return(CursorId);
}
//---------------------------------------------------------------------
public HashSet getListUF(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListUF>:"+PDId);
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(fUSE, Condition.cEQUAL, TermId));
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.getListUF<:"+PDId);
return(CursorId);
}
//---------------------------------------------------------------------
public Cursor ListUF(String TermId) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListUF>:"+PDId);
Conditions Conds=new Conditions();
Conds.addCondition(new Condition(fUSE, Condition.cEQUAL, TermId));
Query Q=new Query(getTabName(), getRecordStructPDThesaur(), Conds, fNAME);
Cursor CursorId=getDrv().OpenCursor(Q);
if (PDLog.isDebug())
    PDLog.Debug("PDThesaurs.ListUF<:"+PDId);
return(CursorId);
}
//---------------------------------------------------------------------
}
