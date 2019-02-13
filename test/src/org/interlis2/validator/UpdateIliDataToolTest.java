package org.interlis2.validator;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;

public class UpdateIliDataToolTest {
    
    private static final String ILIDATA_XML = "test/data/updateIliDataTool/ilidata.xml";

    @Test
    public void update() throws IoxException {
        Settings settings = new Settings();
        settings.setValue(Validator.SETTING_UPDATE_ILIDATA, ILIDATA_XML);
        settings.setValue(Validator.SETTING_DATASETID_TO_UPDATE, "Beispiel2a");
        settings.setValue(Validator.SETTING_NEW_VERSION_OF_DATA, "test/data/updateIliDataTool/newVersionOfData.xtf");
        settings.setValue(Validator.SETTING_REPOSITORY, "test/data/updateIliDataTool/repos1");
        boolean ret = UpdateIliDataTool.update(settings);
        assertTrue(ret);
        
        validateResult();
    }
    
    private void validateResult() throws IoxException {
        XtfReader reader = new XtfReader(new File(ILIDATA_XML));
        
        IoxEvent event = null;
        boolean isTheTestDone = false;
        do {
            event = reader.read();
            
            if (event instanceof ObjectEvent) {
                IoxEvent event1 = event;
                
                IomObject iomObject = ((ObjectEvent) event1).getIomObject();
                
                if (iomObject.getobjectoid().equals("4")) {
                    isTheTestDone = true;
                    // ID
                    assertEquals("Beispiel2a", iomObject.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_id));
                    
                    // Version
                    assertEquals("1", iomObject.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_version));
                    
                    // PrecursorVersion
                    assertEquals("3a", iomObject.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_precursorVersion));
                    
                    // File/FileFormat
                    IomObject files = iomObject.getattrobj(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_files, 0);
                    IomObject file = files.getattrobj(ch.interlis.models.DatasetIdx16.DataFile.tag_file, 0);
                    assertEquals("sub/newVersionOfData.xtf", file.getattrvalue(ch.interlis.models.DatasetIdx16.File.tag_path));
                    assertEquals("c55e611e5896f50cace4d2a5db1c4d34", file.getattrvalue(ch.interlis.models.DatasetIdx16.File.tag_md5));
                    assertEquals("application/interlis+xml;version=2.3", files.getattrvalue(ch.interlis.models.DatasetIdx16.DataFile.tag_fileFormat));                    
                    
                    // Owner
                    assertEquals(CreateIliDataTool.getOwnerByCurrentUser(), iomObject.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_owner));
                    
                    // Baskets
                    IomObject baskets = iomObject.getattrobj(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_baskets, 0);
                    
                    // ModelName
                    IomObject model = baskets.getattrobj(ch.interlis.models.DatasetIdx16.Metadata.tag_model, 0);
                    assertEquals("Beispiel2.Bodenbedeckung", model.getattrvalue(ch.interlis.models.DatasetIdx16.ModelLink.tag_name));
                    assertEquals(CreateIliDataTool.getOwnerByCurrentUser(), baskets.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_owner));
                    assertEquals("b1", baskets.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata.tag_localId));
                    assertEquals("1", baskets.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_version));
                    
                    // 2. Basket
                    IomObject baskets2 = iomObject.getattrobj(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_baskets, 1);
                    assertEquals("cb3817b2-ebb9-4346-a406-0e30c81eff7d", baskets2.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_id));
                    IomObject model2 = baskets2.getattrobj(ch.interlis.models.DatasetIdx16.Metadata.tag_model, 0);
                    assertEquals("Beispiel2.GebaeudeRegister", model2.getattrvalue(ch.interlis.models.DatasetIdx16.ModelLink.tag_name));
                    assertEquals(CreateIliDataTool.getOwnerByCurrentUser(), baskets2.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_owner));
                    assertEquals("1", baskets2.getattrvalue(ch.interlis.models.DatasetIdx16.Metadata.tag_version));
                }
            }
        } while (!(event instanceof EndTransferEvent));
        // Wenn kein Test durchgef�hrt wurde
        assertTrue(isTheTestDone);
        
        // Validate Updated IliDataXml
        boolean runValidation = Validator.runValidation(new String[] { ILIDATA_XML }, null);
        assertTrue(runValidation);
    }
    
}