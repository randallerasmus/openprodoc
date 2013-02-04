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

/*
 * ConfigConection.java
 *
 * Created on 22-nov-2010, 19:46:32
 */

package prodocsetup;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JProgressBar;
import prodoc.DriverGeneric;
import prodoc.PDException;
import prodoc.ProdocFW;

/**
 *
 * @author jhierrot
 */
public class CreateMetadata extends javax.swing.JFrame
{
private static HashMap TransList=new HashMap();
private static String AppLang=null;
Vector Trace=new Vector();
DefaultListModel LM=null;
ActThread Act;

/** Creates new form ConfigConection */
public CreateMetadata()
{
initComponents();
setLocationRelativeTo(null);
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        AcceptButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        AttrTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        LangCode = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        RootPasswordTF = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        RepUrlTF = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        RepEncrypCB = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        RepNameTF = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        MainKeyTF = new javax.swing.JTextField();
        RepUserTF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        RepPassTF = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        RepTypeCB = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        RepParamTF = new javax.swing.JTextField();
        DateFormat = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TimeStampFormat = new javax.swing.JTextField();
        EvolTab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(TT("Prodoc_Metadata_Creation"));
        setResizable(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(TT("Prodoc_Metadata_Creation"));

        AcceptButton.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        AcceptButton.setText(TT("Ok"));
        AcceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AcceptButtonActionPerformed(evt);
            }
        });

        CancelButton.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        CancelButton.setText(TT("Cancel"));
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        AttrTab.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText(TT("Administrator_Password"));

        LangCode.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        LangCode.setText("EN");
        LangCode.setToolTipText("EN, ES, PT (ISO code two character for language for the default customization )");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setText(TT("Default_Language_Code"));

        RootPasswordTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RootPasswordTF.setToolTipText("Password for user administrator \"root\" created during installation. It is possible to define several users with several administrator permissions, \"root\" is only the default administrator");

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText(TT("Repository_User"));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setText(TT("Repository_Url"));

        RepUrlTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepUrlTF.setToolTipText("IP or URI of the repository (depending on the kind of repository can be a local mounted path, a database, and ftp, etc..). It must be visible using the same name(URI) for any \"client\"(that is installation of a Web o Swing Client");

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText(TT("Repository_Encrypted"));

        RepEncrypCB.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepEncrypCB.setToolTipText("Boolean value, when checked, the documents in the repository will be encrypted");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setText(TT("Default_Repository_Name"));

        RepNameTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepNameTF.setText("Reposit");
        RepNameTF.setToolTipText("Descriptive name of the default documents repository. Later it will be possible to create additional repositories with different characteristics");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setText(TT("Main_Key"));

        MainKeyTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        MainKeyTF.setToolTipText("Main key used to encrypt several elements in OPD, including document repositories without native support for encription. It should contain characters with lower and upper case and numbers, with a size big enough to offer a reasonable security");

        RepUserTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepUserTF.setToolTipText("User with permissions for the repository.(required depending on Repository type)");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText(TT("Repository_User_Password"));

        RepPassTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepPassTF.setToolTipText("Password of the User with permissions for the repository. Both fields will be empty if the repository is a Filesystem. (required depending on Repository type)");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setText(TT("Repository_Type"));

        RepTypeCB.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepTypeCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FS", "DDBB", "FTP" }));
        RepTypeCB.setToolTipText("One of the supported Repository Types  (required)\n      FS: Filesystem (can be a mounted disk or server) identified by a path\n      FTP: Ftp Server\n      BLOB: Storing of documents as BLOB field in a Database that can be the same used to store metadata or another one.\nl");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText(TT("Repository_Aditional_Param"));

        RepParamTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        RepParamTF.setToolTipText("Additional parameters that can be different depending on the repository type (required depending on Repository type). For BLOB it is:\n\"JDBC Driver name;Table Name\"\nIe.: \n    com.mysql.jdbc.Driver;TableBlob\n");

        DateFormat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        DateFormat.setText("dd/MM/yyyy");
        DateFormat.setToolTipText("Format to display and read date fields in default customization, according to formats of the Java formatter");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setText(TT("TimeStamp_Format"));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setText(TT("Date_Format"));

        TimeStampFormat.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TimeStampFormat.setText("dd/MM/yyyy  HH:mm:ss");
        TimeStampFormat.setToolTipText("Format to display and read timestamp fields in default customization, according to formats of the Java formatter");

        javax.swing.GroupLayout AttrTabLayout = new javax.swing.GroupLayout(AttrTab);
        AttrTab.setLayout(AttrTabLayout);
        AttrTabLayout.setHorizontalGroup(
            AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttrTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TimeStampFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MainKeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepUserTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LangCode, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RootPasswordTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepPassTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepUrlTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepParamTF, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepTypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RepEncrypCB))
                .addGap(18, 18, 18))
        );

        AttrTabLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {DateFormat, LangCode, MainKeyTF, RepNameTF, RepParamTF, RepPassTF, RepUrlTF, RepUserTF, RootPasswordTF, TimeStampFormat});

        AttrTabLayout.setVerticalGroup(
            AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AttrTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AttrTabLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2))
                    .addComponent(RootPasswordTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AttrTabLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3))
                    .addComponent(LangCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AttrTabLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4))
                    .addComponent(TimeStampFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(DateFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AttrTabLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel6))
                    .addComponent(MainKeyTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AttrTabLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel7))
                    .addComponent(RepNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(RepEncrypCB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RepUrlTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RepUserTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RepPassTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(RepTypeCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(AttrTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RepParamTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab(TT("Parameters_config"), AttrTab);

        EvolTab.setEnabled(false);
        EvolTab.setFocusable(false);
        EvolTab.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jList1.setModel(getListModel());
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout EvolTabLayout = new javax.swing.GroupLayout(EvolTab);
        EvolTab.setLayout(EvolTabLayout);
        EvolTabLayout.setHorizontalGroup(
            EvolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EvolTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EvolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE))
                .addContainerGap())
        );
        EvolTabLayout.setVerticalGroup(
            EvolTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EvolTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(TT("Installation_Process"), EvolTab);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AcceptButton)
                        .addGap(9, 9, 9)
                        .addComponent(CancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AcceptButton)
                    .addComponent(CancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_CancelButtonActionPerformed
    {//GEN-HEADEREND:event_CancelButtonActionPerformed
dispose();
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void AcceptButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AcceptButtonActionPerformed
    {//GEN-HEADEREND:event_AcceptButtonActionPerformed
if (RootPasswordTF.getText()==null || RootPasswordTF.getText().length()==0)
    {
    Message("RootPassword "+TT("Required_field"));
    return;
    }
if (LangCode.getText()==null || LangCode.getText().length()==0)
    {
    Message("LangCode "+TT("Required_field"));
    return;
    }
if (TimeStampFormat.getText()==null || TimeStampFormat.getText().length()==0)
    {
    Message("TimeStampFormat "+TT("Required_field"));
    return;
    }
if (DateFormat.getText()==null || DateFormat.getText().length()==0)
    {
    Message("DateFormat "+TT("Required_field"));
    return;
    }
if (MainKeyTF.getText()==null || MainKeyTF.getText().length()==0)
    {
    Message("MainKey "+TT("Required_field"));
    return;
    }
if (RepNameTF.getText()==null || RepNameTF.getText().length()==0)
    {
    Message("Repository Name "+TT("Required_field"));
    return;
    }
if (RepUrlTF.getText()==null || RepUrlTF.getText().length()==0)
    {
    Message("Repository URL "+TT("Required_field"));
    return;
    }
try {
CreateMetadataStructure();
} catch(Exception ex)
    {
    Message(ex.getLocalizedMessage());
    AcceptButton.setEnabled(true);
    CancelButton.setEnabled(true);
    return;
    }
System.exit(0);
    }//GEN-LAST:event_AcceptButtonActionPerformed

/**
* @param args the command line arguments
*/
public static void main(String args[])
{
if (args.length>0)
    AppLang=args[0];
java.awt.EventQueue.invokeLater(new Runnable()
    {
    public void run()
    {
    new CreateMetadata().setVisible(true);
    }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AcceptButton;
    private javax.swing.JPanel AttrTab;
    private javax.swing.JButton CancelButton;
    private javax.swing.JTextField DateFormat;
    private javax.swing.JPanel EvolTab;
    private javax.swing.JTextField LangCode;
    private javax.swing.JTextField MainKeyTF;
    private javax.swing.JCheckBox RepEncrypCB;
    private javax.swing.JTextField RepNameTF;
    private javax.swing.JTextField RepParamTF;
    private javax.swing.JTextField RepPassTF;
    private javax.swing.JComboBox RepTypeCB;
    private javax.swing.JTextField RepUrlTF;
    private javax.swing.JTextField RepUserTF;
    private javax.swing.JTextField RootPasswordTF;
    private javax.swing.JTextField TimeStampFormat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

//----------------------------------------------------------
static protected String TT(String Text)
{
String Lang=getLang();
if (Lang.equals("EN"))
    return(Text.replace("_", " "));
Properties Trans=getProperties(Lang);
if (Trans==null)
    return(Text.replace("_", " "));
String Translation=Trans.getProperty(Text);
if (Translation==null)
    return(Text.replace("_", " "));
else
    return(Translation);
}
//----------------------------------------------------------
static private String getLang()
{
if (AppLang==null)
    {
    Locale locale = Locale.getDefault();
    AppLang=locale.getLanguage().toUpperCase();
    }
return(AppLang);
}
//----------------------------------------------------------
static private Properties getProperties(String Lang)
{
Properties Trans=(Properties)TransList.get(Lang);
if (Trans!=null)
    return(Trans);
InputStream f=null;
try {
f= CreateMetadata.class.getResourceAsStream("lang/Text_"+Lang+".properties");
Trans=new Properties();
Trans.load(f);
f.close();
} catch (Exception ex)
    {
    if (f!=null)
        {
        try {
            f.close();
        } catch (IOException ex1)
            {
            ex1.printStackTrace();
            }
        }
    return(null);
    }
TransList.put(Lang, Trans);
return(Trans);
}
//---------------------------------------------------------------------
/**
 * 
 * @param pMessage
 */
static public void Message(String pMessage)
{
DialogInfo DI=new DialogInfo(null, true);
DI.SetMessage(pMessage);
DI.setLocationRelativeTo(null);
DI.setVisible(true);
}
//---------------------------------------------------------------------
private void CreateMetadataStructure() throws PDException
{
Vector Trace=new Vector();
EvolTab.setEnabled(true);
EvolTab.setFocusable(true);
jTabbedPane1.setSelectedComponent(EvolTab);
jProgressBar1.setMaximum(35);
AcceptButton.setEnabled(false);
CancelButton.setEnabled(false);
repaint();
Act=new ActThread();
Act.SetParam(Trace, getListModel(), jProgressBar1, jList1);
Act.start();
String RootPassword=RootPasswordTF.getText().trim();
String DefLang=LangCode.getText().trim().toUpperCase();
if (!(DefLang.equalsIgnoreCase("ES") || DefLang.equalsIgnoreCase("EN")))
    throw new PDException(TT("Unsuported_Language")+":"+DefLang);
String DefTimeFormat=TimeStampFormat.getText().trim();
String DefDateFormat=DateFormat.getText().trim();
String MainKey=MainKeyTF.getText();
String RepName=RepNameTF.getText();
boolean RepEncrypt=RepEncrypCB.isSelected();
String RepUrl=RepUrlTF.getText();
String RepUser=RepUserTF.getText();
String RepPassword=RepPassTF.getText();
String RepType=(String)RepTypeCB.getSelectedItem();
String RepParam=RepParamTF.getText();
ActInst ActI=new ActInst();
ActI.SetParam(RootPassword, DefLang, DefTimeFormat,DefDateFormat, MainKey, RepName,
        RepEncrypt, RepUrl, RepUser, RepPassword, RepType, RepParam, Trace, this);
ActI.start();
}
//--------------------------------------------------------------
private DefaultListModel getListModel()
{
if (LM==null)
    LM=new DefaultListModel();
return LM;
}
//--------------------------------------------------------------
/** internal class to update evolution
 * 
 */
public class ActInst extends Thread
{
String RootPassword;
String DefLang;
String DefTimeFormat;
String DefDateFormat;
String MainKey;
String RepName;
boolean RepEncrypt;
String RepUrl;
String RepUser;
String RepPassword;
String RepType;
String RepParam;
Vector Trace;
CreateMetadata Form;
public void SetParam(String pRootPassword, String pDefLang, String pDefTimeFormat,
                    String pDefDateFormat, String pMainKey, String pRepName,
                    boolean pRepEncrypt, String pRepUrl, String pRepUser,
                    String pRepPassword, String pRepType, String pRepParam, Vector pTrace,
                    CreateMetadata pForm)
{
RootPassword=pRootPassword;
DefLang=pDefLang;
DefTimeFormat=pDefTimeFormat;
DefDateFormat=pDefDateFormat;
MainKey=pMainKey;
RepName=pRepName;
RepEncrypt=pRepEncrypt;
RepUrl=pRepUrl;
RepUser=pRepUser;
RepPassword=pRepPassword;
RepType=pRepType;
RepParam=pRepParam;
Trace=pTrace;
Form=pForm;
}
//------------------------------------
public void run()
{
DriverGeneric Sesion=null;
try {
ProdocFW.InitProdoc("PD", "Prodoc.properties");
Sesion=ProdocFW.getSession("PD", "Install", "Install");
Sesion.Install(RootPassword, DefLang, DefTimeFormat,DefDateFormat, MainKey, RepName,
        RepEncrypt, RepUrl, RepUser, RepPassword, RepType, RepParam, Trace);
ProdocFW.freeSesion("PD", Sesion);
ProdocFW.ShutdownProdoc("PD");
sleep(60);
Act.setFinished(true);
Message(TT("OpenProdoc_Server_created_correctly"));
dispose();
} catch (Exception e)
    {
    e.printStackTrace();
    Act.setFinished(true);
    Message(TT("Error_creating_metadata_structure")+":"+e.getLocalizedMessage());
    AcceptButton.setEnabled(true);
    CancelButton.setEnabled(true);
    if (Sesion!=null)
        {
        try {
        ProdocFW.freeSesion("PD", Sesion);
        ProdocFW.ShutdownProdoc("PD");
        } catch (PDException ex)
            {
            ex.printStackTrace();
            }
        }
    }
}
//------------------------------------
}

//--------------------------------------------------------------
/** internal class to update evolution
 * 
 */
public class ActThread extends Thread
{
Vector Tr;
JProgressBar PB;
JList LT;
DefaultListModel LM;
int Position=0;
private boolean Finished=false;
//------------------------------------
public void SetParam(Vector pTr, DefaultListModel pLM, JProgressBar pPB, JList pLT)
{
Tr=pTr;
LM=pLM;
PB=pPB;
LT=pLT;
}
//------------------------------------
 @Override
public void run()
{
while (!Finished)
    {
    PB.setValue(Position);
    for (; Position<Tr.size(); Position++)
        {
        LM.addElement((String)Tr.elementAt(Position));
        }
    LT.setSelectedIndex(Position-1);
    LT.ensureIndexIsVisible(Position-1);
    repaint();
    try {
        sleep(10);
    } catch (InterruptedException ex)
        {
        }
    }
}
//------------------------------------
/**
 * @param pFinished the Finished to set
 */
public void setFinished(boolean pFinished)
{
Finished = pFinished;
}
//------------------------------------
}

//---------------------------------------------------------------------
}
