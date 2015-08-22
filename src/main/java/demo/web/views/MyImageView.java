package demo.web.views;

import com.google.gwt.thirdparty.guava.common.io.Files;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Notification;
import demo.jpa.entities.Image;
import demo.jpa.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Created by victor on 8/20/15.
 */
@SpringView(name = MyImageView.VIEW_NAME)
public class MyImageView extends MVerticalLayout implements View {
    public static final String VIEW_NAME = "image";
    private static final Logger LOG = LoggerFactory.getLogger(MyImageView.class);
    @Autowired
    private ImageRepository imageRepository;
    private final UploadField upload = new UploadField() {
        @Override
        protected void updateDisplay() {
            byte[] fileData = (byte[]) getValue();
            String mimeType = getLastMimeType();
            String fileName = getLastFileName();
            if (mimeType.equals("image/png") || mimeType.equals("image/jpg")
                    || mimeType.equals("image/jpeg")) {
                Resource resource = new StreamResource(
                        () -> new ByteArrayInputStream(fileData),
                        ""
                ) {
                    @Override
                    public String getMIMEType() {
                        return mimeType;
                    }
                };
                Embedded embedded = new Embedded("Image:" + fileName + "(" + getLastFileSize() + ") bytes", resource);
                addComponent(embedded);
                Image image = new Image();
                image.setName(fileName);
                image.setData(fileData);
                image = imageRepository.save(image);
                MyImageView.LOG.info("Saving {} to DB", fileName);
                Notification.show("Image saved.");
                embedded.addClickListener(event -> removeComponent(embedded));
                embedded.setAlternateText("Remove");
            } else {
                super.updateDisplay();
            }
        }
    };

    @PostConstruct
    public void init() {
        File tmpDir = Files.createTempDir();
        upload.setFieldType(UploadField.FieldType.BYTE_ARRAY);
        upload.setDisplayUpload(true);
        upload.setAcceptFilter("image/*");
        upload.setFileFactory((s, s1) -> new File(tmpDir, s));
        upload.setCaption("Upload, field type: " + upload.getFieldType());
        Button b = new MButton("Show Status", event -> Notification.show("Value: " + upload.getValue()));
        Button c = new MButton("Submit", event -> upload.commit());
        addComponents(
                upload,
                b,
                c
        );
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public ImageRepository getImageRepository() {
        return this.imageRepository;
    }

    public void setImageRepository(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }
}
