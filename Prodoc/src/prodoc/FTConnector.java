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

import java.io.InputStream;
import java.util.ArrayList;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

/**
 *
 * @author Joaquín Hierro
 */
public abstract class FTConnector
{
//-------------------------------------------
private String Server=null;
private String User=null;
private String Password=null;
private String Param=null;
private String FullText=null;
private String FileMetadata=null;
final public static String F_TYPE="Type";
final public static String F_ID="Id";
final public static String F_VER="Ver";
final public static String F_FULLTEXT="FullText";
final public static String F_DOCMETADATA="DocMetadata";
final public static String F_PREFIX="F_";

final public static int MAXRESULTS=1000;

/**
 *
 * @param pUrl
 * @param User
 * @param password
 */
FTConnector(String pServer, String pUser, String pPassword, String pParam)
{
Server=pServer;
User=pUser;
Password=pPassword;
Param=pParam;    
}
/**
 *
 * @throws prodoc.PDException
 */
abstract protected void Create() throws PDException;
/**
 *
 * @throws PDException
 */
abstract protected void Destroy() throws PDException;
/**
 *
 * @throws prodoc.PDException
 */
abstract protected void Connect() throws PDException;
/**
 *
 * @throws prodoc.PDException
 */
abstract protected void Disconnect() throws PDException;
/**
 *
 * @param Type
 * @param Id
 * @param Bytes
 * @param sMetadata
 * @return
 * @throws PDException
 */
abstract protected int Insert(String Type, String Id, InputStream Bytes, Record sMetadata) throws PDException;
protected void Insert(PDDocs Doc) throws PDException
{
StoreGeneric Rep=Doc.getDrv().getRepository(Doc.getReposit());  
try {    
Rep.Connect();
Insert(Doc.getDocType(), Doc.getPDId(), Rep.Retrieve(Doc.getPDId(), Doc.getVersion()), Doc.getRecSum());
//Rep.Retrieve(getPDId(), getVersion(), OutBytes);
Rep.Disconnect();
Rep=null;
} catch(Exception ex)
    {
    Rep.Disconnect();
    throw new PDException(ex.getLocalizedMessage());
    }    
}
/**
 *
 * @param Id
 * @throws prodoc.PDException
 */
abstract protected void Delete(String Id) throws PDException;
    /**
     *
     * @param Type
     * @param Id
     * @param sDocMetadata
     * @param sBody
     * @param sMetadata
     * @return
     * @throws PDException
     */
abstract protected ArrayList<String> Search(String Type, String Id, String sDocMetadata, String sBody, String sMetadata) throws PDException;
//-------------------------------------------------------------------------
/**
* @return the Server
*/
protected String getServer()
{
return Server;
}
//-------------------------------------------------------------------------
/**
* @param Server the Server to set
*/
protected void setServer(String Server)
{
this.Server = Server;
}
//-------------------------------------------------------------------------
/**
* @return the User
*/
protected String getUser()
{
return User;
}
//-------------------------------------------------------------------------
/**
* @param User the User to set
*/
protected void setUser(String User)
{
this.User = User;
}
//-------------------------------------------------------------------------
/**
* @return the Password
*/
protected String getPassword()
{
return Password;
}
//-------------------------------------------------------------------------
/**
* @param Password the Password to set
*/
protected void setPassword(String Password)
{
this.Password = Password;
}
//-------------------------------------------------------------------------
/**
* @return the Param
*/
protected String getParam()
{
return Param;
}
//-------------------------------------------------------------------------
/**
* @param Param the Param to set
*/
protected void setParam(String Param)
{
this.Param = Param;
}
//------------------------------------------------------------------------
protected String Convert(InputStream Bytes) throws PDException
{  
try {                
ContentHandler textHandler=new BodyContentHandler();
Metadata metadata=new Metadata();
Parser parser=new AutoDetectParser();
ParseContext context=new ParseContext();
parser.parse(Bytes, textHandler, metadata, context);
FileMetadata="";
for (String key : metadata.names()) 
    FileMetadata+=key+"="+metadata.get(key)+"\n";
FullText=textHandler.toString();
} catch (Exception ex)
    {
    PDException.GenPDException("Error_extracting_content_from_doc", ex.getLocalizedMessage());
    }

return(FullText); 
}
//------------------------------------------------------------------------
/**
* @return the Fulltext
*/
public String getFullText()
{
return FullText;
}
//------------------------------------------------------------------------
/**
* @return the Metadata
*/
public String getFileMetadata()
{
return FileMetadata;
}
//------------------------------------------------------------------------

}
