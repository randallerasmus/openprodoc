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
 * author: Joaquin Hierro      2011
 * 
 */

package prodoc;

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prodoc.security.*;

/**
 *
 * @author jhierrot
 */
abstract public class DriverGeneric
{
/**
 *
 */
private String URL;
/**
 *
 */
private String PARAM;
/**
 *
 */
private String DBUser;
/**
 *
 */
private String DBPassword;
/**
 *
 */
private boolean Locked=false;
/**
 *
 */
private Date TimeUsed;
/**
 *
 */
private Date TimeLocked;
/**
 *
 */
private PDUser User=null;
/**
 *
 */
private boolean InTransaction=false;
/**
 *
 */
private HashMap OpenCur=null;
/**
 *
 */
private HashMap Class2Reposit=null;
/**
 *
 */
private HashMap ListReposit=null;
/**
 *
 */
private HashMap ListAuth=null;
private final char[] ServerKey = "Esta es la clave".toCharArray();
private static HashMap TransList=new HashMap();
private String AppLang=null;
private static String DefAppLang=null;
private PDCustomization PDCust=null;

static final public String S_LOGIN   ="LOGIN";  // Ok
static final public String S_LOGOUT  ="LOGOUT"; // Ok
static final public String S_SELECT  ="SELECT";  // Ok
static final public String S_INSERT  ="INSERT";  // Ok
static final public String S_DELETE  ="DELETE";   // Ok
static final public String S_UPDATE  ="UPDATE";   // Ok
static final public String S_CREATE  ="CREATE";    // Ok
static final public String S_DROP    ="DROP";     // Ok
static final public String S_ALTER   ="ALTER";    // Ok
static final public String S_ALTERDEL="ALTERDEL";   // Ok
static final public String S_INTEGRIT="INTEGRIT";  // Ok
static final public String S_INTEGRIT2="INTEGRIT2";  // Ok
static final public String S_INITTRANS="INITTRANS";  // Ok
static final public String S_COMMIT   ="COMMIT";    // Ok
static final public String S_CANCEL   ="CANCEL";    // Ok
static final public String S_UNLOCK   ="UNLOCK"; // Ok

static final public String S_DELFILE   ="DELFILE";    
static final public String S_RENFILE   ="RENFILE";    
static final public String S_RETRIEVEFILE   ="RETRIEVEFILE";    
static final public String S_INSFILE   ="INSFILE";    

private TreeMap AllTaskTrans=null;
private TreeMap AllTaskNoTrans=null;
/**
 *
 * @param pURL
 * @param pPARAM
 * @param pUser
 * @param pPassword
 */
public DriverGeneric(String pURL, String pPARAM, String pUser, String pPassword)
{
URL=pURL.trim();
PARAM=pPARAM.trim();
DBUser=pUser.trim();
DBPassword=Decode(pPassword);
}
//--------------------------------------------------------------------------
/**
* @return the URL
*/
public String getURL()
{
return URL;
}
//--------------------------------------------------------------------------
/**
* @return the PARAM
*/
public String getPARAM()
{
return PARAM;
}
//--------------------------------------------------------------------------
/**
* @return the DBUser
*/
public String getDBUser()
{
return DBUser;
}
//--------------------------------------------------------------------------
/**
* @return the DBPassword
*/
protected String getDBPassword()
{
return DBPassword;
}
//--------------------------------------------------------------------------
/**
 * 
 * @return
 */
public boolean isLocked()
{
return(Locked);
}
//--------------------------------------------------------------------------
/**
 *
 */
public void Lock()
{
Locked=true;
TimeLocked=new Date();
}
//--------------------------------------------------------------------------
/**
 *
 */
public void UnLock()
{
Locked=false;
TimeLocked=null;
}
//--------------------------------------------------------------------------
/**
* @return the TimeUsed
*/
public Date getTimeUsed()
{
return TimeUsed;
}
//--------------------------------------------------------------------------

/**
* @return the TimeLocked
*/
public Date getTimeLocked()
{
return TimeLocked;
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void delete() throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @return
 */
abstract public boolean isConnected();
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields
 * @throws PDException
 */
abstract protected void CreateTable(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @throws PDException
 */
abstract protected void DropTable(String TableName) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param NewAttr New field to add
 * @throws PDException
 */
abstract protected void AlterTableAdd(String TableName, Attribute NewAttr, boolean IsVer) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param OldAttr old field to delete
 * @throws PDException
 */
abstract protected void AlterTableDel(String TableName, String OldAttr) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param Fields
 * @throws PDException
 */
abstract protected void InsertRecord(String TableName, Record Fields) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param DelConds 
 * @throws PDException
 */
abstract protected void DeleteRecord(String TableName, Conditions DelConds) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName
 * @param NewFields
 * @param UpConds 
 * @throws PDException
 */
abstract protected void UpdateRecord(String TableName, Record NewFields, Conditions UpConds) throws PDException;
//--------------------------------------------------------------------------
/**
 *
 * @param TableName1
 * @param Field1
 * @param TableName2
 * @param Field2 
 * @throws PDException
 */
abstract protected void AddIntegrity(String TableName1, String Field1, String TableName2, String Field2) throws PDException;
//--------------------------------------------------------------------------

    /**
     *
     * @param TableName1
     * @param Field11
     * @param Field12
     * @param TableName2
     * @param Field21
     * @param Field22
     * @throws PDException
     */
    abstract protected void AddIntegrity(String TableName1, String Field11, String Field12, String TableName2, String Field21, String Field22) throws PDException;

/**
 * 
 * @param RootPassword 
 * @param DefLang 
 * @param RepUrl 
 * @param DefTimeFormat 
 * @param DefDateFormat 
 * @param RepName 
 * @param MainKey 
 * @param RepEncrypt 
 * @param Trace 
 * @param RepUser 
 * @param RepParam 
 * @param RepType 
 * @param RepPassword 
 * @throws PDException
 */
public void Install(String RootPassword, String DefLang, String DefTimeFormat, 
                    String DefDateFormat, String MainKey, String RepName,
                    boolean RepEncrypt, String RepUrl, String RepUser,
                    String RepPassword, String RepType, String RepParam, Vector Trace) throws PDException
{
//-------- Tables creation ---------------------------------------------------
Trace.add("Starting Installation....");
PDServer Se=new PDServer(this);
Se.Install();
Trace.add("Server Table created");
PDRoles Ro=new PDRoles(this);
Ro.Install();
Trace.add("Roles Table created");
PDCustomization Cu=new PDCustomization(this);
Cu.Install();
Trace.add("Customization Table created");
PDAuthenticators Au=new PDAuthenticators(this);
Au.Install();
Trace.add("Authenticators Table created");
//PDUser U=new PDUser(this);
getUser().Install();
PDACL A=new PDACL(this);
A.Install();
Trace.add("ACL Table created");
PDGroups G=new PDGroups(this);
G.Install();
Trace.add("Groups Table created");
PDRepository R=new PDRepository(this);
R.Install();
Trace.add("Repository Table created");
PDObjDefs D=new PDObjDefs(this);
D.Install();
Trace.add("Definitions Table created");
//PDFolders F=new PDFolders(this);
PDFolders.Install(this);
Trace.add("Folder Table created");
PDMimeType M=new PDMimeType(this);
M.Install();
Trace.add("MimeType Table created");
PDDocs DD=new PDDocs(this);
DD.Install();
Trace.add("Doces Table created");
PDTrace T=new PDTrace(this);
T.Install();
Trace.add("Trace Table created");
PDEvent E=new PDEvent(this);
E.Install();
Trace.add("Event Table created");
PDMessage ME=new PDMessage(this);
ME.Install();
Trace.add("Message Table created");
PDThesaur TE=new PDThesaur(this);
TE.Install();
Trace.add("Thesaur Table created");
PDTasksDefEvent TDE=new PDTasksDefEvent(this);
TDE.Install();
Trace.add("PDTasksDefEvent Table created");
PDTasksCron TDC=new PDTasksCron(this);
TDC.Install();
Trace.add("PDTasksCron Table created");
PDTasksExec TEx=new PDTasksExec(this);
TEx.Install();
Trace.add("PDTasksExec Table created");
PDTasksExecEnded TExEnd=new PDTasksExecEnded(this);
TExEnd.Install();
Trace.add("PDTasksExecEnded Table created");
//-------- Tables creation 2 phase -------------------------------------
getUser().InstallMulti();
Trace.add("User relations created");
G.InstallMulti();
Trace.add("Groups relations created");
A.InstallMulti();
Trace.add("ACL relations created");
R.InstallMulti();
Trace.add("Repository relations created");
PDFolders.InstallMulti(this);
Trace.add("Folders relations created");
DD.InstallMulti();
Trace.add("Docs relations created");
D.InstallMulti();
Trace.add("Definitions relations created");
M.InstallMulti();
Trace.add("Mime relations created");
ME.InstallMulti();
Trace.add("Message relations created");
PDThesaur.InstallMulti(this);
Trace.add("Thesaur relations created");
TDE.InstallMulti();
Trace.add("PDTasksDefEvent relations created");
TDC.InstallMulti();
Trace.add("PDTasksCron relations created");
TEx.InstallMulti();
Trace.add("PDTasksExec relations created");
TExEnd.InstallMulti();
Trace.add("PDTasksExecEnded relations created");
//--- insertion of objects --------------------------------------------
this.IniciarTrans(); 
//----- Servidor ---------
Se.setName("Prodoc");
Se.setKey(Encode(MainKey));
Se.setVersion(getVersion());
Se.insert();
Trace.add("Server element created");
//----- Autorization ---------
Au.setName("Prodoc");
Au.setDescription("Default Validation in this system");
Au.setAuthType(PDAuthenticators.tOPD);
Au.insert();
Trace.add("Authenticator element created");
//----- Styles ---------
Cu.setName("Prodoc");
Cu.setDescription("Prodoc default style");
Cu.setDateFormat(DefDateFormat); // "dd-MM-yyyy"
Cu.setTimeFormat(DefTimeFormat); // "dd-MM-yyyy HH:mm:ss"
Cu.setLanguage(DefLang);
Cu.setStyle("");
Cu.setSwingStyle("\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12,\"Arial\",0,12");
Cu.insert();
Cu.setName("ProdocAdm");
Cu.setDescription("Prodoc Admin style");
Cu.setStyle("adm");
Cu.insert();
Trace.add("Customization element created");
//----- Roles ---------
Ro.setName("Users");
Ro.setDescription("Perfil de usuarios estándar");
Ro.setAllowCreateFolder(true);
Ro.setAllowMaintainFolder(true);
Ro.setAllowCreateDoc(true);
Ro.setAllowMaintainDoc(true);
Ro.insert();
Ro.setName("Administrators");
Ro.setDescription("Perfil de usuarios administradores");
Ro.setAllowCreateUser(true);
Ro.setAllowMaintainUser(true);
Ro.setAllowCreateGroup(true);
Ro.setAllowMaintainGroup(true);
Ro.setAllowCreateAcl(true);
Ro.setAllowMaintainAcl(true);
Ro.setAllowCreateRole(true);
Ro.setAllowMaintainRole(true);
Ro.setAllowCreateObject(true);
Ro.setAllowMaintainObject(true);
Ro.setAllowCreateRepos(true);
Ro.setAllowMaintainRepos(true);
Ro.setAllowCreateFolder(true);
Ro.setAllowMaintainFolder(true);
Ro.setAllowCreateDoc(true);
Ro.setAllowMaintainDoc(true);
Ro.setAllowCreateMime(true);
Ro.setAllowMaintainMime(true);
Ro.setAllowCreateAuth(true);
Ro.setAllowMaintainAuth(true);
Ro.setAllowCreateCustom(true);
Ro.setAllowMaintainCustom(true);
Ro.setAllowCreateThesaur(true);
Ro.setAllowMaintainThesaur(true);
Ro.setAllowCreateTask(true);
Ro.setAllowMaintainTask(true);
Ro.insert();
Trace.add("Roles elements created");
// --- Acl Administrators ---
PDACL AclAdmin=new PDACL(this);
AclAdmin.setName("Administrators");
AclAdmin.setDescription("Permision to maintain Administrators");
AclAdmin.insert();
// --- Acl Public  ---
PDACL Aclpublic=new PDACL(this);
Aclpublic.setName("Public");
Aclpublic.setDescription("Public Information");
Aclpublic.insert();
// --- Acl DocsCommon  ---
PDACL DocsCommon=new PDACL(this);
DocsCommon.setName("DocsCommon");
DocsCommon.setDescription("Public Docs Types");
DocsCommon.insert();
Trace.add("ACL elements created");
// --- Group all ---
PDGroups Grp=new PDGroups(this);
Grp.setName("All");
Grp.setDescription("All users group");
Grp.setAcl("Public");
Grp.insert();
Grp.setName("Administrators");
Grp.setDescription("Users with administration rights");
Grp.setAcl("Public");
Grp.insert();
Trace.add("Groups elements created");
//---- Definitions folder -----------------------
D.setName(PDFolders.getTableName());
D.setDescription("Base Folder");
D.setActive(true);
D.setClassType(PDObjDefs.CT_FOLDER);
D.setParent(PDFolders.getTableName());
D.setACL(DocsCommon.getName());
D.insert();
Record Rec=PDFolders.getRecordStructPDFolder();
Rec.initList();
for (int i = 0; i < Rec.NumAttr(); i++)
    {
    D.addAtribute(Rec.nextAttr());
    }
Trace.add("Definitions elements created");
// --- Folders -----------------------------------------
PDFolders.CreateBaseRootFold(this);
PDFolders UsersFold=new PDFolders(this);
//UsersFold.setPDId(PDFolders.USERSFOLDER);
//UsersFold.setTitle(PDFolders.USERSFOLDER);
//UsersFold.setParentId(PDFolders.ROOTFOLDER);
//UsersFold.setACL("Public");
// --- Administrator ---
PDUser Usu=new PDUser(this);
Usu.setName("root");
Usu.setPassword(RootPassword);
Usu.setDescription("Administrator");
Usu.setActive(true);
Usu.setValidation(Au.getName());
Usu.setRole(Ro.getName());
Usu.setCustom(Cu.getName()); // ProdocAdm
Usu.insert();
Grp.addUser("root");
Trace.add("Administrator created");

// --- Group Administrator ---
AclAdmin.addUser( "root", PDACL.pDELETE);
AclAdmin.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addUser( "root", PDACL.pDELETE);
Aclpublic.addGroup("Administrators", PDACL.pDELETE);
Aclpublic.addGroup("All", PDACL.pREAD);
DocsCommon.addGroup("Administrators", PDACL.pDELETE);
DocsCommon.addGroup("All", PDACL.pUPDATE);
R.setName(RepName);
R.setDescription("Repositorio por defecto");
R.setEncrypted(RepEncrypt);
R.setURL(RepUrl);
R.setUser(RepUser);
R.setPassword(Encode(RepPassword));
R.setRepType(RepType);
R.setParam(RepParam);
R.insert();
Trace.add("Repository element created");
//---- Definitions Doc -----------------
D.setName(PDDocs.getTableName());
D.setDescription("Base Document");
D.setActive(true);
D.setClassType(PDObjDefs.CT_DOC);
D.setParent(PDDocs.getTableName());
D.setACL(DocsCommon.getName());
D.setReposit(R.getName());
D.insert();
Record RecD=DD.getRecordStruct();
RecD.initList();
for (int i = 0; i < RecD.NumAttr(); i++)
    {
    D.addAtribute(RecD.nextAttr());
    }
Trace.add("Document elements created");
//----------MIME Types -------------------------------------------
File FileImp=new File("ex/defs.opd");
ProcessXML(FileImp, PDFolders.ROOTFOLDER);
TE.CreateRootThesaur();
//----------------------
this.CerrarTrans();

Trace.add("Installation finished");
}
//--------------------------------------------------------------------------
public void Update(boolean UpMetadataInc, Vector Trace)  throws PDException
{
PDServer Serv=new PDServer(this);
Serv.Load("Prodoc");
if (Serv.getVersion().equalsIgnoreCase("1.0"))
    {
    Trace.add("NO Update possible. Already 1.0 version");    
    return;
    }
Trace.add("Update started");    
if (Serv.getVersion().equalsIgnoreCase("0.7")) //******************************
    {
    // -- Adding Thesaurus tables -----------------------------------------    
    PDThesaur TE=new PDThesaur(this);
    try {
    TE.Install();
    Trace.add("Thesaur Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    PDThesaur.InstallMulti(this);
    Trace.add("Thesaur relations created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    try {
    TE.CreateRootThesaur();
    Trace.add("Root Term created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Adding new Roles attributes -------------------------------------    
    PDRoles Rup=new PDRoles(this);
    Attribute Attr1=Rup.getRecordStruct().getAttr(PDRoles.fALLOWCREATETHESAUR).Copy();
    Attr1.setValue(false);
    try {
    AlterTableAdd(PDRoles.getTableName(), Attr1, false);
    Trace.add(PDRoles.fALLOWCREATETHESAUR+"created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Attribute Attr2=Rup.getRecordStruct().getAttr(PDRoles.fALLOWMAINTAINTHESAUR).Copy();
    try {
    Attr2.setValue(false);
    AlterTableAdd(PDRoles.getTableName(), Attr2, false);
    Trace.add(PDRoles.fALLOWMAINTAINTHESAUR+"created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating administrators Roles granting new attributes ----------------    
    try {
    Record NewAdmvals=new Record();
    Attr1.setValue(true);
    NewAdmvals.addAttr(Attr1);
    Attr2.setValue(true);
    NewAdmvals.addAttr(Attr2);
    Conditions AdmRole=new Conditions();
    AdmRole.addCondition(new Condition(Rup.fNAME, Condition.cEQUAL, "Administrators"));
    UpdateRecord(Rup.getTabName(), NewAdmvals, AdmRole);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating Repository version ------------------------------------------    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("0.8");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(Serv.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Update ended ----
    Trace.add("Updated to 0.8");
    }
Serv.Load("Prodoc"); 
if (Serv.getVersion().equalsIgnoreCase("0.8")  //*****************************
    || Serv.getVersion().equalsIgnoreCase("0.8.1"))
    {
    // -- Adding Task tables ----------------------------------    
    PDTasksCron TC=new PDTasksCron(this);
    try {
    TC.Install();
    Trace.add("TasksCron Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksDefEvent TE=new PDTasksDefEvent(this);
    try {
    TE.Install();
    Trace.add("Tasks Event Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksExec TExec=new PDTasksExec(this);
    try {
    TExec.Install();
    Trace.add("Tasks Pending Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    PDTasksExecEnded TExecEnd=new PDTasksExecEnded(this);
    try {
    TExecEnd.Install();
    Trace.add("Tasks Ended Table created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Adding new Roles attributes -----------------------------    
    PDRoles Rup=new PDRoles(this);
    Attribute Attr1=Rup.getRecordStruct().getAttr(PDRoles.fALLOWCREATETASK).Copy();
    Attr1.setValue(false);
    try {
    AlterTableAdd(PDRoles.getTableName(), Attr1, false);
    Trace.add(PDRoles.fALLOWCREATETASK+"created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    Attribute Attr2=Rup.getRecordStruct().getAttr(PDRoles.fALLOWMAINTAINTASK).Copy();
    try {
    Attr2.setValue(false);
    AlterTableAdd(PDRoles.getTableName(), Attr2, false);
    Trace.add(PDRoles.fALLOWMAINTAINTASK+"created");
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating administrators Roles granting new attributes -----    
    try {
    Record NewAdmvals=new Record();
    Attr1.setValue(true);
    NewAdmvals.addAttr(Attr1);
    Attr2.setValue(true);
    NewAdmvals.addAttr(Attr2);
    Conditions AdmRole=new Conditions();
    AdmRole.addCondition(new Condition(Rup.fNAME, Condition.cEQUAL, "Administrators"));
    UpdateRecord(Rup.getTabName(), NewAdmvals, AdmRole);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    // -- Updating Repository version -----    
    try {
    Record RecServ=new Record();    
    Attribute RV=Serv.getRecord().getAttr(PDServer.fVERSION).Copy();
    RV.setValue("1.0");
    RecServ.addAttr(RV);
    Conditions ServDef=new Conditions();
    ServDef.addCondition(new Condition(PDServer.fNAME, Condition.cEQUAL, "Prodoc"));
    UpdateRecord(Serv.getTabName(), RecServ, ServDef);    
    } catch (PDException pDException)
        {
        if (!UpMetadataInc)
            throw pDException;
        else
            Trace.add(pDException.getLocalizedMessage());            
        }
    //--- Update ended ----
    Trace.add("Updated to 1.0");    
    }
Trace.add("Update finished");
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException 
 */
public void Uninstall() throws PDException
{
PDACL A=new PDACL(this);
try {
A.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando ACL:"+pDException.getLocalizedMessage());
    }
PDDocs DD=new PDDocs(this);
try {
DD.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Documentos:"+pDException.getLocalizedMessage());
    }
PDFolders F=new PDFolders(this);
try {
F.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Carpetas:"+pDException.getLocalizedMessage());
    }
PDObjDefs D=new PDObjDefs(this);
try {
D.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Documentos:"+pDException.getLocalizedMessage());
    }
PDGroups G=new PDGroups(this);
try {
G.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Grupos:"+pDException.getLocalizedMessage());
    }
PDUser U=new PDUser(this);
try {
    U.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando usuario:"+pDException.getLocalizedMessage());
    }
PDMimeType M=new PDMimeType(this);
try {
M.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Tipos Mime:"+pDException.getLocalizedMessage());
    }
PDRepository R=new PDRepository(this);
try {
R.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Repositorio:"+pDException.getLocalizedMessage());
    }
PDRoles Ro=new PDRoles(this);
try {
Ro.unInstallMulti();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Roles:"+pDException.getLocalizedMessage());
    }
//--- uninstall 2 phase ---
try {
PDAuthenticators Au=new PDAuthenticators(this);
Au.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Autentificadores:"+pDException.getLocalizedMessage());
    }
try {
PDTrace T=new PDTrace(this);
T.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando traza:"+pDException.getLocalizedMessage());
    }
try {
PDEvent E=new PDEvent(this);
E.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Eventos:"+pDException.getLocalizedMessage());
    }
try {
PDMessage ME=new PDMessage(this);
ME.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Mensajes:"+pDException.getLocalizedMessage());
    }
try {
DD.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Documentos 2:"+pDException.getLocalizedMessage());
    }
try {
F.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Carpetas 2:"+pDException.getLocalizedMessage());
    }
try {
D.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Definiciones Documentos 2:"+pDException.getLocalizedMessage());
    }
try {
M.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Tipos Mime 2:"+pDException.getLocalizedMessage());
    }
try {
G.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Grupos 2:"+pDException.getLocalizedMessage());
    }
try {
A.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando ACL 2:"+pDException.getLocalizedMessage());
    }
try {
U.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando usuario 2:"+pDException.getLocalizedMessage());
    }
try {
R.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Repositorio:"+pDException.getLocalizedMessage());
    }
try {
Ro.unInstall();
} catch (PDException pDException)
    {
    PDLog.Error("Error desinstalando Roles:"+pDException.getLocalizedMessage());
    }
}
//--------------------------------------------------------------------------
/**
* @return the InTransaction
*/
public boolean isInTransaction()
{
return InTransaction;
}
//--------------------------------------------------------------------------
/**
 * @param pInTransaction 
*/
public void setInTransaction(boolean pInTransaction)
{
InTransaction = pInTransaction;
}
//--------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void IniciarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @throws PDException
 */
abstract public void CerrarTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 * 
 * @throws PDException
 */
abstract public void AnularTrans() throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @param Search
 * @return a CursorCode, stored by the drivers
 * @throws PDException
 */
abstract public Cursor OpenCursor(Query Search) throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @return
 */
private String genCursorName()
{
return ("Cursor"+Math.random());
}
////-----------------------------------------------------------------------------------
//private int IdentifyCursor(String CursorIdent)  throws PDException
//{
//if (CursorIdent.length()<7)
//    throw new PDException("Cursor no identificado");
//int NumCur=Integer.parseInt(CursorIdent.substring(6));
//if (NumCur<0||NumCur>=getOpenCur().size())
//    throw new PDException("Cursor Inexistente");
//return(NumCur);
//}
//-----------------------------------------------------------------------------------
/**
* @return the OpenCur
*/
private HashMap getOpenCur()
{
if (OpenCur==null)
    OpenCur=new HashMap();
return OpenCur;
}
//-----------------------------------------------------------------------------------
///**
//* @return the OpenCur
//*/
//private Vector getFieldsCur()
//{
//if (FieldsCur==null)
//    FieldsCur=new Vector();
//return FieldsCur;
//}
//-----------------------------------------------------------------------------------
/**
 *
 * @param rs
 * @param Fields
 * @return
 */
protected Cursor StoreCursor(Object rs, Record Fields)
{
Cursor Cur=new Cursor();
Cur.setCursorId(genCursorName());
Cur.setFieldsCur(Fields);
Cur.setResultSet(rs);
getOpenCur().put(Cur.getCursorId(), Cur);
return(Cur);
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @param CursorIdent
 * @return
 * @throws PDException 
 */
protected Object getCursor(Cursor CursorIdent) throws PDException
{
return(CursorIdent.getResultSet());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @throws PDException
 */
protected void delCursor(Cursor CursorIdent) throws PDException
{
if (getOpenCur().containsKey(CursorIdent.getCursorId()))    
   getOpenCur().remove(CursorIdent.getCursorId());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @return
 * @throws PDException
 */
abstract public Record NextRec(Cursor CursorIdent)  throws PDException;
//-----------------------------------------------------------------------------------
/**
 *
 * @param CursorIdent
 * @throws PDException
 */
abstract public void CloseCursor(Cursor CursorIdent) throws PDException;
//-----------------------------------------------------------------------------------
/**
 * Logged user acording his authenticator
 * @param userName
 * @param Password
 * @throws PDException
 */
void Assign(String userName, String Password) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Assign>:"+userName);
if (!userName.equalsIgnoreCase("Install"))
    {
    if (getUser().Load(userName)==null)
        PDExceptionFunc.GenPDException("User_or_password_incorrect", userName);
    AuthGeneric Auth=getAuthentic(getUser().getValidation());
    Auth.Authenticate(userName, Password);
    getUser().LoadAll(userName);
    getPDCust().Load(getUser().getCustom());
    setAppLang(getPDCust().getLanguage());
    }
else
    {
    getUser().setName("Install");
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Assign<:"+userName);
}
//---------------------------------------------------------------------
/**
 * Protected method to create a Special taskUser
 */
protected void AssignTaskUser() throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.AssignTaskUser");
User=new PDUser(this);
User.CreateTaskUser();
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @throws PDException
 */
public void RefreshUser() throws PDException
{
getUser().LoadAll(getUser().getName());
}
//-----------------------------------------------------------------------------------
/**
 * @return the User
 * @throws PDException
*/
public PDUser getUser() throws PDException
{
if (User==null)
   User=new PDUser(this);
return User;
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param tableName 
 * @param TypeDef
 * @param TypeRecs
 * @throws PDException
 */
protected void LoadDef(String tableName, ArrayList TypeDef, ArrayList TypeRecs) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.LoadDef>:"+tableName);
PDObjDefs Def=new PDObjDefs(this);
Def.setParent(tableName);
while (!Def.getParent().equalsIgnoreCase(Def.getName()))
    {
    Def.Load(Def.getParent());
    TypeDef.add(Def.getRecord().Copy());
    TypeRecs.add(Def.GetAttrDef().Copy());
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.LoadDef<:"+tableName);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param Obj
 * @throws PDException
 */
public void Export(PrintWriter FileOut, ObjPD Obj) throws PDException
{
FileOut.println(Obj.getTabName());
Export(FileOut, Obj.getRecord());
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param RecList
 * @throws PDException
 */
public void Export(PrintWriter FileOut, String ObjName, Vector RecList) throws PDException
{
for (int i = 0; i < RecList.size(); i++)
    {
    FileOut.println(ObjName);
    Record Rec = (Record)RecList.elementAt(i);
    Export(FileOut, Rec);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param ObjName
 * @param CursorId
 * @throws PDException
 */
public void Export(PrintWriter FileOut, String ObjName, Cursor CursorId) throws PDException
{
Record Rec=this.NextRec(CursorId);
while (Rec!=null)
    {
    FileOut.println(ObjName);
    Export(FileOut, Rec);
    Rec=this.NextRec(CursorId);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileOut
 * @param Rec
 * @throws PDException
 */
public void Export(PrintWriter FileOut, Record Rec) throws PDException
{
FileOut.println(Rec.NumAttr());
Rec.initList();
for (int i = 0; i < Rec.NumAttr(); i++)
    {
    Attribute A=Rec.nextAttr();
    FileOut.println(A.getName());
    FileOut.println(A.Export());
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param FileIn
 * @param Rec
 * @throws PDException
 */
public void Import(BufferedReader FileIn, Record Rec) throws PDException
{
try {
String NumStr = FileIn.readLine();
int NumAtt = Integer.parseInt(NumStr);
String NomAtt;
String ValAtt;
for (int i = 0; i < NumAtt; i++)
    {
    NomAtt=FileIn.readLine();
    Attribute Att = Rec.getAttr(NomAtt);
    if (Att==null)
        PDExceptionFunc.GenPDException("Unknown_attibute",NomAtt);
    ValAtt=FileIn.readLine();
    Att.Import(ValAtt);
    }
} catch (IOException ex)
    {
    PDException.GenPDException(ex.getLocalizedMessage(),null);
    }
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Rep
 * @throws PDException
 */
public void CreateRep(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.CreateRep>:"+Rep.getName());
StoreGeneric st=ConstrucStore(Rep);
st.Connect();
st.Create();
st.Disconnect();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.CreateRep<");
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Rep
 * @throws PDException
 */
public void DestroyRep(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DestroyRep>:"+Rep.getName());
StoreGeneric st=ConstrucStore(Rep);
st.Connect();
st.Destroy();
st.Disconnect();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DestroyRep<");}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Rep
 * @return
 * @throws PDException
 */
private StoreGeneric ConstrucStore(PDRepository Rep) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore>:"+Rep.getName());
StoreGeneric st=null;
String RepTyp=Rep.getRepType();
if (RepTyp.equals(PDRepository.tFS))
    st=new StoreFS(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tBBDD))
    st=new StoreDDBB(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tFTP))
    st=new Storeftp(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tREFURL))
    st=new StoreRefURL(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else if (RepTyp.equals(PDRepository.tS3))
    st=new StoreAmazonS3(Rep.getURL(), Rep.getUser(), /*Decode*/(Rep.getPassword()), Rep.getParam(), Rep.isEncrypted());
else
    PDException.GenPDException("Repository_type_unsuported", RepTyp);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstrucStore<");
return(st);
}
//-----------------------------------------------------------------------------------
/**
 * Search, for a DocType, the define Reposit
 * @param docType
 * @return
 * @throws PDException
 */
protected String getAssignedRepos(String docType) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAssignedRepos>:"+docType);
String Rep=null;
if (Class2Reposit==null)
    Class2Reposit=new HashMap();
else
    {
    Rep=(String)Class2Reposit.get(docType);
    if (Rep!=null)
        {
        if (PDLog.isDebug())
            PDLog.Debug("DriverGeneric.getAssignedRepos yet Instanced:"+Rep);
        return(Rep);
        }
    }
PDObjDefs Def=new PDObjDefs(this);
Def.Load(docType);
Rep=Def.getReposit();
Class2Reposit.put(docType, Rep);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAssignedRepos<");
return(Rep);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param RepName
 * @return
 * @throws PDException
 */
protected StoreGeneric getRepository(String RepName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository>:"+RepName);
StoreGeneric Rep=null;
Rep=(StoreGeneric)getListReposit().get(RepName);
if (Rep!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("DriverGeneric.Rep yet Instanced:"+Rep.getServer());
    return (Rep);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Rep new Instance:"+RepName);
PDRepository RepDesc=new PDRepository(this);
RepDesc.Load(RepName);
Rep=ConstrucStore(RepDesc);
getListReposit().put(RepName, Rep);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getRepository<:"+RepName);
return(Rep);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param ValidatName
 * @return
 * @throws PDException
 */
private AuthGeneric getAuthentic(String ValidatName) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAuthentic>:"+ValidatName);
AuthGeneric Auth=null;
Auth=(AuthGeneric)getListAuth().get(ValidatName);
if (Auth!=null)
    {
    if (PDLog.isDebug())
        PDLog.Debug("DriverGeneric.Auth yet Instanced:"+ValidatName);
    return (Auth);
    }
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.Auth new Instance:"+ValidatName);
PDAuthenticators RepDesc=new PDAuthenticators(this);
RepDesc.Load(ValidatName);
Auth=ConstructAuthentic(RepDesc);
getListAuth().put(ValidatName, Auth);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.getAuthentic<:"+ValidatName);
return(Auth);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param Auth
 * @return
 */
private AuthGeneric ConstructAuthentic(PDAuthenticators Auth) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstructAuthentic>:"+Auth.getName());
AuthGeneric st=null;
String AuthType=Auth.getAuthType();
if (AuthType.equals(PDAuthenticators.tOPD))
    st=new AuthOPD( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam(), this);
else if (AuthType.equals(PDAuthenticators.tBBDD))
    st=new AuthDDBB( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else if (AuthType.equals(PDAuthenticators.tLDAP))
    st=new AuthLDAP( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else if (AuthType.equals(PDAuthenticators.tSO))
    st=new AuthSO( Auth.getURL(), Auth.getUser(), Decode(Auth.getPassword()), Auth.getParam());
else
    PDException.GenPDException("Authentication_type_unsuported", AuthType);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ConstructAuthentic<:"+Auth.getName());
return(st);
}
//-----------------------------------------------------------------------------------
/**
* @return the ListAuth
*/
private HashMap getListAuth()
{
if (ListAuth==null)
    ListAuth=new HashMap();
return ListAuth;
}
//-----------------------------------------------------------------------------------
/**
 * @return the ListReposit
 */
private HashMap getListReposit()
{
if (ListReposit==null)
    ListReposit=new HashMap();
return ListReposit;
}
//-----------------------------------------------------------------------------------
/**
 * 
 * @param UserName
 * @param OldPassword
 * @param NewPassword
 * @throws PDException
 */
public void ChangePassword(String UserName, String OldPassword, String NewPassword) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ChangePassword>:"+UserName);
PDUser U2cp=new PDUser(this);
if (U2cp.Load(UserName)==null)
    PDExceptionFunc.GenPDException("User_or_password_incorrect", UserName);
AuthGeneric Auth=getAuthentic(U2cp.getValidation());
Auth.Authenticate(UserName, OldPassword);
if (U2cp.getValidation().equals(PDAuthenticators.tOPD))
    Auth.UpdatePass(UserName, Encode(NewPassword));
else
    Auth.UpdatePass(UserName, NewPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.ChangePassword<:"+UserName);
}
//-----------------------------------------------------------------------------------
/**
 *
 * @param UserName
 * @param NewPassword
 * @throws PDException
 */
public void SetPassword(String UserName, String NewPassword) throws PDException
{
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.SetPassword>:"+UserName);
PDUser U2cp=new PDUser(this);
if (U2cp.Load(UserName)==null)
    PDExceptionFunc.GenPDException("User_or_password_incorrect", UserName);
AuthGeneric Auth=getAuthentic(U2cp.getValidation());
//if (U2cp.getValidation().equals(PDAuthenticators.tOPD))
//    Auth.UpdatePass(UserName, Encode(NewPassword));
//else
    Auth.UpdatePass(UserName, NewPassword);
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.SetPassword<:"+UserName);
}
//-----------------------------------------------------------------------------------
/**
 * Returns the conector version
 * @return
 */
static public String getVersion()
{
return("1.0");
}
private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
//-----------------------------------------------------------------------------------
private static final char[] Key = "Esta es la clave".toCharArray();
/**
 *
 * @param ToBeEncoded
 * @return
 */
static protected String Encode(String ToBeEncoded)
{
if (ToBeEncoded==null || ToBeEncoded.length()==0)
    return(ToBeEncoded);
StringBuilder S=new StringBuilder(ToBeEncoded);
for (int i = 0; i < S.length(); i++)
    {
    S.setCharAt(i, (char)(S.charAt(i)^ Key[i%16]));
    }
byte[] buf=S.toString().getBytes();
char[] chars = new char[2 * buf.length];
for (int i = 0; i < buf.length; ++i)
    {
    chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
    chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
    }
return new String(chars);
}
//-------------------------------------------------------------------------
/**
 *
 * @param ToBeDecoded
 * @return
 */
static private String Decode(String ToBeDecoded)
{
if (ToBeDecoded==null || ToBeDecoded.length()==0)
    return(ToBeDecoded);
byte[] buf=new byte[ToBeDecoded.length()/2];
for (int i = 0; i < buf.length; i++)
    {
    String tmp=ToBeDecoded.substring(i*2, i*2+2);
    Integer n=Integer.parseInt(ToBeDecoded.substring(i*2, i*2+2), 16);
    buf[i] = (byte) Integer.parseInt(ToBeDecoded.substring(i*2, i*2+2), 16) ;
    }
StringBuffer S=new StringBuffer(new String(buf));
for (int i = 0; i < S.length(); i++)
    {
    S.setCharAt(i, (char)(S.charAt(i)^ Key[i%16]));
    }
return(S.toString());
}
//-------------------------------------------------------------------------
private byte[] EncodeBlock(byte[] buff)
{
for (int i = 0; i <buff.length; i++)
    {
    buff[i]=(byte)(buff[i]^ ServerKey[i%16]);
    }
return buff;
}
//-------------------------------------------------------------------------
private byte[] DecodeBlock(byte[] buff)
{
for (int i = 0; i <buff.length; i++)
    {
    buff[i]=(byte)(buff[i]^ ServerKey[i%16]);
    }
return buff;
}
//-------------------------------------------------------------------------
/**
 * Creates a properties file to be used by any client, including to create metadata repository
 * @param FileName 
 * @param UserName 
 * @param ConnectName 
 * @param UrlServer 
 * @param JDBCClass 
 * @param Password 
 * @throws Exception
 */
static public void generateProps(String FileName, String ConnectName, String UrlServer, String UserName, String Password, String JDBCClass) throws Exception
{
ConnectName=ConnectName.trim();
PrintWriter FProps = new PrintWriter(FileName, "UTF-8");
FProps.println("#------------------------------------------------------------");
FProps.println("# The Kind of connection to data (JDBC, Remote, ...");
FProps.println(ConnectName+".DATA_TYPE=JDBC");
FProps.println("#----for remote conection -----------------------------------");
FProps.println("# comment/delete User and password lines");
FProps.println("# and uncomment:");
FProps.println("# PD.DATA_TYPE=Remote");
FProps.println("# and uncomment and modify");
FProps.println("# PD.DATA_URL=http://host:8080/_ProdocWeb/Oper");
FProps.println("# where host= IP of OPD server    and ");
FProps.println("#_ProdocWeb= url where deployed OPD J2EE Rest service");
FProps.println("#------------------------------------------------------------");
FProps.println("# URL form conection");
FProps.println(ConnectName+".DATA_URL="+UrlServer.trim());
FProps.println("# connection user");
FProps.println(ConnectName+".DATA_USER="+UserName.trim());
FProps.println("# connection password");
FProps.println(ConnectName+".DATA_PASSWORD="+Encode(Password.trim()));
FProps.println("# minimum number of sessions for metadata");
FProps.println(ConnectName+".DATA_MIN=1");
FProps.println("# maximum number of sessions for metadata");
FProps.println(ConnectName+".DATA_MAX=30");
FProps.println("# Aditional param to be interpreted by every driver (the jdbc class for DATA_TYPE=JDBC)");
FProps.println(ConnectName+".DATA_PARAM="+JDBCClass.trim());
FProps.println("# Timeout (in ms) before the conection is closed");
FProps.println(ConnectName+".DATA_TIMEOUT=300000");
FProps.println("#--------------------------------------------------------------");
FProps.println("# minimum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MIN=1");
FProps.println("# maximum number of sessions in documents repository");
FProps.println(ConnectName+".PR_MAX=10");
FProps.println("# Timeout (in ms) before the conection is closed");
FProps.println(ConnectName+".PR_TIMEOUT=300000");
FProps.println("# TraceLevel LOGLEVELERROR=0, LOGLEVELINFO=1, LOGLEVELDEBUG=2");
FProps.println("TRACELEVEL=0");
FProps.println("# Path to the log4j properties file");
FProps.println("# Beware that the TRACELEVEL has priority over level defined in the file");
FProps.println("TRACECONF=log4j.properties");
FProps.println("#--------------------------------------------------------------");
FProps.println("# Elements related to Tasks");
FProps.println("#--------------------------------------------------------------");
FProps.println("# Category of task to generate and execute in this computer (* = all categories");
FProps.println("#PD.TaskCategory=*");
FProps.println("# Pooling frecuency for Generation in miliseconds");
FProps.println("#PD.TaskSearchFreq=120000");
FProps.println("# Pooling frecuency for Execution  in miliseconds");
FProps.println("#PD.TaskExecFreq=60000");
FProps.flush();
FProps.close();
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Text
 * @return
 */
public String TT(String Text)
{
if (Text==null)
    return("NullPointerException");
String Lang=getAppLang();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Text
 * @return
 */
public static String DefTT(String Text)
{
String Lang=getDefAppLang();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//-------------------------------------------------------------------------
/**
 * 
 * @param Lang
 * @param Text
 * @return
 */
public static String DefTT(String Lang, String Text)
{
Lang=Lang.toUpperCase();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text);
String ToTrans;
int Pos=Text.indexOf(':');
String AddText;
if (Pos!=-1)
    {
    ToTrans = Text.substring(0, Pos);
    AddText=Text.substring(Pos);
    }
else
    {
    ToTrans=Text;
    AddText="";
    }
String Translation=Trans.getProperty(ToTrans);
if (Translation==null)
    return(ToTrans.replace("_", " ")+AddText);
else
    return(Translation+AddText);
}
//----------------------------------------------------------
static private Properties getProperties(String Lang)
{
Lang=Lang.toUpperCase();
Properties Trans=(Properties)TransList.get(Lang);
if (Trans!=null)
    return(Trans);
InputStream f=null;
try {
f= DriverGeneric.class.getResourceAsStream("lang/Text_"+Lang+".properties");
Trans=new Properties();
Trans.load(f);
} catch (Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage()+":"+Lang);
    return(null);
    }
finally
    {
    try {
    if (f!=null)
        f.close();
    } catch (Exception ex)
        {
        PDLog.Error(ex.getLocalizedMessage()+":"+Lang);
        }
    }
TransList.put(Lang, Trans);
return(Trans);
}
//----------------------------------------------------------
/**
 * @return the AppLang
 */
public String getAppLang()
{
if (AppLang==null)
    {
    Locale locale = Locale.getDefault();
    setAppLang(locale.getLanguage().toUpperCase());
    }
return AppLang;
}
//----------------------------------------------------------
/**
 * @param pAppLang the AppLang to set
 */
public void setAppLang(String pAppLang)
{
AppLang = pAppLang.toUpperCase();
}
//---------------------------------------------------------------------
/**
 * @return the DefAppLang
 */
public static String getDefAppLang()
{
if (DefAppLang==null)
    {
    Locale locale = Locale.getDefault();
    setDefAppLang(locale.getLanguage().toUpperCase());
    }
return DefAppLang;
}
//---------------------------------------------------------------------
/**
 * @param aDefAppLang the DefAppLang to set
 */
public static void setDefAppLang(String aDefAppLang)
{
DefAppLang = aDefAppLang.toUpperCase();
}
//---------------------------------------------------------------------

/**
 * @return the PDCust
 * @throws PDException  
 */
public PDCustomization getPDCust() throws PDException
{
if (PDCust==null)
   PDCust=new PDCustomization(this);
return PDCust;
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLFile
 * @param ParentFolderId
 * @return Num of elements processed
 * @throws PDException
 */
public int ProcessXML(File XMLFile, String ParentFolderId) throws PDException
{
try {
DocumentBuilder DB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document XMLObjects = DB.parse(XMLFile);
NodeList OPDObjectList = XMLObjects.getElementsByTagName(ObjPD.XML_OPDObject);
Node OPDObject = null;
ObjPD Obj2Build=null;
int Tot=0;
for (int i=0; i<OPDObjectList.getLength(); i++)
    {
    OPDObject = OPDObjectList.item(i);
    Obj2Build=BuildObj(OPDObject);
    if (Obj2Build instanceof PDDocs)
        {
        ((PDDocs)Obj2Build).ImportXMLNode(OPDObject, XMLFile.getAbsolutePath().substring(0, 
                                           XMLFile.getAbsolutePath().lastIndexOf(File.separatorChar)),
                                           ParentFolderId, false);
        Tot++;
        }
    else if (Obj2Build instanceof PDFolders)
            ;  // ((PDFolders)Obj2Build).ImportXMLNode(OPDObject, ParentFolderId, false);
    else
        {
        Obj2Build.ProcesXMLNode(OPDObject);
        Tot++;
        }
    }
DB.reset();
return(Tot);
}catch(Exception ex)
    {
    PDLog.Error(ex.getLocalizedMessage());
    throw new PDException(ex.getLocalizedMessage());
    }
}
//---------------------------------------------------------------------
private ObjPD BuildObj(Node OPDObject) throws PDException
{
NamedNodeMap attributes = OPDObject.getAttributes();
Node namedItem = attributes.getNamedItem("type");
String OPDObjectType=namedItem.getNodeValue();
if (OPDObjectType.equalsIgnoreCase(PDDocs.getTableName()))
    return(new PDDocs(this));
if (OPDObjectType.equalsIgnoreCase(PDFolders.getTableName()))
    return(new PDFolders(this));
if (OPDObjectType.equalsIgnoreCase(PDACL.getTableName()))
    return(new PDACL(this));
if (OPDObjectType.equalsIgnoreCase(PDAuthenticators.getTableName()))
    return(new PDAuthenticators(this));
if (OPDObjectType.equalsIgnoreCase(PDCustomization.getTableName()))
    return(new PDCustomization(this));
if (OPDObjectType.equalsIgnoreCase(PDGroups.getTableName()))
    return(new PDGroups(this));
if (OPDObjectType.equalsIgnoreCase(PDMimeType.getTableName()))
    return(new PDMimeType(this));
if (OPDObjectType.equalsIgnoreCase(PDObjDefs.getTableName()))
    return(new PDObjDefs(this));
if (OPDObjectType.equalsIgnoreCase(PDRepository.getTableName()))
    return(new PDRepository(this));
if (OPDObjectType.equalsIgnoreCase(PDRoles.getTableName()))
    return(new PDRoles(this));
if (OPDObjectType.equalsIgnoreCase(PDUser.getTableName()))
    return(new PDUser(this));
if (OPDObjectType.equalsIgnoreCase(PDTasksDefEvent.getTableName()))
    return(new PDTasksDefEvent(this));
if (OPDObjectType.equalsIgnoreCase(PDTasksCron.getTableName()))
    return(new PDTasksCron(this));
throw new PDException("Inexistent_OPD_object_type");
}
//---------------------------------------------------------------------
public String RemoteOrder(String Order, Document XMLObjects) throws PDException
{
String Result=null;
boolean EndsOk=true;
if (Order.equals(S_SELECT))
    {
    return("<OPD><Result>OK</Result><Data>"+GenVector(XMLObjects)+"</Data></OPD>");
    }
else if (Order.equals(S_DELFILE))
    {
    return("<OPD><Result>OK</Result><Data>"+DeleteFile(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_RENFILE))
    {
    return("<OPD><Result>OK</Result><Data>"+RenameFile(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_UPDATE))
    {
    return("<OPD><Result>OK</Result><Data>"+UpdateRecord(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_INSERT))
    {
    return("<OPD><Result>OK</Result><Data>"+InsertRecord(XMLObjects)+"</Data></OPD>");
    }
else if (Order.equals(S_DELETE))
    {
    return("<OPD><Result>OK</Result><Data>"+DeleteRecord(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_CREATE))
    {
    return("<OPD><Result>OK</Result><Data>"+CreateTable(XMLObjects)+"</Data></OPD>");    
    }
else if (Order.equals(S_DROP))
    {
    return("<OPD><Result>OK</Result><Data>"+DropTable(XMLObjects)+"</Data></OPD>");        
    }
else if (Order.equals(S_ALTER))
    {
    return("<OPD><Result>OK</Result><Data>"+AlterTableAdd(XMLObjects)+"</Data></OPD>");            
    }
else if (Order.equals(S_ALTERDEL))
    {
    return("<OPD><Result>OK</Result><Data>"+AlterTableDel(XMLObjects)+"</Data></OPD>");                
    }
else if (Order.equals(S_INTEGRIT))
    {
    return("<OPD><Result>OK</Result><Data>"+AddIntegrity(XMLObjects)+"</Data></OPD>");                    
    }
else if (Order.equals(S_INTEGRIT2))
    {
    return("<OPD><Result>OK</Result><Data>"+AddIntegrity2(XMLObjects)+"</Data></OPD>");                    
    }
else if (Order.equals(S_INITTRANS))
    {
    IniciarTrans();
    }
else if (Order.equals(S_COMMIT))
    {
    CerrarTrans();
    }
else if (Order.equals(S_CANCEL))
    {
    AnularTrans();
    }
else 
    return("<OPD><Result>KO</Result><Msg>Unknown Order</Msg></OPD>");
return("<OPD><Result>OK</Result></OPD>");
}        
//---------------------------------------------------------------------
/**
 * Opens a Cursor y generates a "Vector" with all the results
 * @param XMLObjects Query as XML
 * @return XML with the <Data> contect
 * @throws PDException 
 */
private String GenVector(Document XMLObjects) throws PDException
{
StringBuilder Res=new StringBuilder();
Query Q=new Query(XMLObjects);    
Cursor C=OpenCursor(Q);
Record R=NextRec(C);
while (R!=null)
    {
    Res.append(R.toXMLt());
    R=NextRec(C);
    }
CloseCursor(C);
delCursor(C);
return(Res.toString());
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return
 * @throws PDException 
 */
private String InsertRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
InsertRecord(Tab, R);
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return
 * @throws PDException 
 */
private String DeleteRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("DelConds");
OPDObject = OPDObjectList.item(0);
Conditions C= new Conditions(OPDObject);
DeleteRecord(Tab, C);
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return
 * @throws PDException 
 */
private String UpdateRecord(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
OPDObjectList = XMLObjects.getElementsByTagName("UpConds");
OPDObject = OPDObjectList.item(0);
Conditions C= new Conditions(OPDObject);
UpdateRecord(Tab, R, C);
return("");
}
//---------------------------------------------------------------------

private String CreateTable(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rec");
OPDObject = OPDObjectList.item(0);
Record R=Record.CreateFromXML(OPDObject);
CreateTable(Tab, R);
return("");
}
//---------------------------------------------------------------------

private String DropTable(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
DropTable(Tab);
return("");
}
//---------------------------------------------------------------------

private String AlterTableAdd(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("attr");
OPDObject = OPDObjectList.item(0);
Attribute A=new Attribute(OPDObject);
OPDObjectList = XMLObjects.getElementsByTagName("IsVer");
OPDObject = OPDObjectList.item(0);
String IsVer=OPDObject.getTextContent();
AlterTableAdd(Tab, A, IsVer.equals("1"));
return("");
}
//---------------------------------------------------------------------
private String AlterTableDel(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab");
Node OPDObject = OPDObjectList.item(0);
String Tab=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("OldAttr");
OPDObject = OPDObjectList.item(0);
String OldAttr=OPDObject.getTextContent();
AlterTableDel(Tab, OldAttr);
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return
 * @throws PDException 
 */
private String AddIntegrity(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab1");
Node OPDObject = OPDObjectList.item(0);
String Tab1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field1");
OPDObject = OPDObjectList.item(0);
String Field1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Tab2");
OPDObject = OPDObjectList.item(0);
String Tab2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field2");
OPDObject = OPDObjectList.item(0);
String Field2=OPDObject.getTextContent();
AddIntegrity(Tab1, Field1, Tab2, Field2);
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return
 * @throws PDException 
 */
private String AddIntegrity2(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Tab1");
Node OPDObject = OPDObjectList.item(0);
String Tab1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field11");
OPDObject = OPDObjectList.item(0);
String Field11=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field12");
OPDObject = OPDObjectList.item(0);
String Field12=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Tab2");
OPDObject = OPDObjectList.item(0);
String Tab2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field21");
OPDObject = OPDObjectList.item(0);
String Field21=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Field22");
OPDObject = OPDObjectList.item(0);
String Field22=OPDObject.getTextContent();
AddIntegrity(Tab1, Field11,Field12, Tab2, Field21, Field22);
return("");
}
//---------------------------------------------------------------------
/**
 * Allows to decide how to download file
 * @return true ir Driver is remote
 */
protected boolean IsRemote()
{
return(false);
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return 
 */
private String DeleteFile(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Id");
Node OPDObject = OPDObjectList.item(0);
String Id=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver");
OPDObject = OPDObjectList.item(0);
String Ver=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Rep");
OPDObject = OPDObjectList.item(0);
String RepName=OPDObject.getTextContent();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.DeleteFile:"+Id+"/"+Ver);    
StoreGeneric Rep=getRepository(RepName);
if (!Rep.IsRef())
    {
    Rep.Connect();
    Rep.Delete(Id, Ver);
    Rep.Disconnect();
    }
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param XMLObjects
 * @return 
 */
private String RenameFile(Document XMLObjects) throws PDException
{
NodeList OPDObjectList = XMLObjects.getElementsByTagName("Id1");
Node OPDObject = OPDObjectList.item(0);
String Id1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver1");
OPDObject = OPDObjectList.item(0);
String Ver1=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Id2");
OPDObject = OPDObjectList.item(0);
String Id2=OPDObject.getTextContent();
OPDObjectList = XMLObjects.getElementsByTagName("Ver2");
OPDObject = OPDObjectList.item(0);
String Ver2=OPDObject.getTextContent();
if (PDLog.isDebug())
    PDLog.Debug("DriverGeneric.RenameFile:"+Id1+"/"+Ver1+"->"+Id2+"/"+Ver2);    
PDDocs doc=new PDDocs(this);
doc.setPDId(Id1);
doc.LoadVersion(Id1, Ver1);
StoreGeneric Rep=getRepository(doc.getReposit());
if (!Rep.IsRef())
    {
    Rep.Connect();
    Rep.Rename(Id1, Ver1, Id2, Ver2);
    Rep.Disconnect();
    }
return("");
}
//---------------------------------------------------------------------
/**
 * 
 * @param Id
 * @param Ver
 * @param FileData
 * @throws PDException 
 */
public void InsertFile(String Id, String Ver, InputStream FileData) throws PDException
{
PDDocs Doc=new PDDocs(this);
Doc.Load(Id);
StoreGeneric St=getRepository(Doc.getReposit());
St.Insert(Id, Ver, FileData);
}
//-----------------------------------------------------------------   
static public String Codif(String Text)
{
return(Text.replace('<', '^').replace("%", "¡1").replace("&", "¡2"));
}    
//-----------------------------------------------------------------   
static public String DeCodif(String Text)
{
return(Text.replace('^', '<').replace("¡1", "%").replace("¡2","&"));
}    
//---------------------------------------------------------------------
/**
 * Returns a list of Transactional tasks ORDERED for selected fold type AND operation
 *    including parent Fold types
 * @param folderType Folder type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 * @throws prodoc.PDException in any error
 */
protected ArrayList<PDTasksDefEvent> getFoldTransThreads(String folderType, String MODE) throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDFolders f=new PDFolders(this,folderType);
ArrayList TypList=f.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns a list of Non Transactional tasks ORDERED for selected fold type AND operation
 *    including parent types
 * @param folderType Folder type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 */
protected ArrayList getFoldNoTransThreads(String folderType, String MODE) throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDFolders f=new PDFolders(this,folderType);
ArrayList TypList=f.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskNoTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
* @return the AllTaskTrans
*/
private TreeMap getAllTaskTrans() throws PDException
{
if (AllTaskTrans==null)    
    LoadAllTaks();
return AllTaskTrans;
}
//---------------------------------------------------------------------
/**
* @return the AllTaskTrans
*/
private TreeMap getAllTaskNoTrans() throws PDException
{
if (AllTaskNoTrans==null)    
    LoadAllTaks();
return AllTaskNoTrans;
}
//---------------------------------------------------------------------
/**
 * Loads AllTaskNoTrans and AllTaskTrans the first time
 */
private void LoadAllTaks() throws PDException
{
AllTaskTrans=new TreeMap();
AllTaskNoTrans=new TreeMap();
PDTasksDefEvent TE=new PDTasksDefEvent(this);
Cursor C=TE.SearchLike("");
Record R=NextRec(C);
while (R!=null)
    {
    PDTasksDefEvent TDE=new PDTasksDefEvent(this);  
    TDE.assignValues(R);
    if (TDE.isActive())
        {
        if (TDE.isTransact())
            AllTaskTrans.put(TDE.getObjType()+"/"+TDE.getEvenType()+("0000"+TDE.getEvenOrder()).substring(0, 5), TDE);
        else
            AllTaskNoTrans.put(TDE.getObjType()+"/"+TDE.getEvenType()+("0000"+TDE.getEvenOrder()).substring(0, 5), TDE);
        }
    R=NextRec(C);
    }
CloseCursor(C);
}
//---------------------------------------------------------------------
/**
 * Returns a list of Transactional tasks ORDERED for selected Doc type AND operation
 *    including parent types
 * @param docType Doc type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 */

 protected ArrayList<PDTasksDefEvent> getDocTransThreads(String docType, String MODE)  throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDDocs D=new PDDocs(this,docType);
ArrayList TypList=D.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
/**
 * Returns a list of Non Transactional tasks ORDERED for selected DOC type AND operation
 *    including parent types
 * @param docType Doc type
 * @param MODE  Kind of operation (INS, DEL, UP)
 * @return a list that can be empty
 */
 protected ArrayList getDocNoTransThreads(String docType, String MODE) throws PDException
{
ArrayList TotalTask=new ArrayList();    
PDDocs D=new PDDocs(this,docType);
ArrayList TypList=D.getTypeDefs();
for (int i = TypList.size()-1; i >=0 ; i--)
    {
    String TypName=(String)((Record)TypList.get(i)).getAttr(PDObjDefs.fNAME).getValue();
    for (Iterator it = getAllTaskNoTrans().subMap(TypName+"/"+MODE, TypName+"/"+MODE+"999999").values().iterator(); it.hasNext();)
        TotalTask.add(it.next());
    }
return TotalTask;
}
//---------------------------------------------------------------------
static public String getHelpLang(String UserLang)
{
if (UserLang.equalsIgnoreCase("ES") || UserLang.equalsIgnoreCase("PT") || UserLang.equalsIgnoreCase("CT") )    
    return("ES");
else
    return("EN");
}
//---------------------------------------------------------------------
}