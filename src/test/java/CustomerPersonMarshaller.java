
import org.springframework.stereotype.Component;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Component
public class CustomerPersonMarshaller {

    public <T> T unmarshal(String fileString, Class clazz) throws UnsupportedEncodingException {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            //InputStream source = new ByteArrayInputStream(fileString.getBytes("UTF-8"));

            //JAXBElement<T> root = unmarshaller.unmarshal(null, clazz);

            return null; //root.getValue();

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> String marshal(T object, Class clazz) {

        try {


            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(object, outputStream);
            String marshalledString = new String (outputStream.toByteArray());

            return marshalledString;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    public <T> String marshalWithRootMissing(T object, Class clazz, String elementName) {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Marshaller marshaller = jaxbContext.createMarshaller();

            QName qName = new QName(clazz.getPackage().getName(), elementName);
            JAXBElement<T> root = new JAXBElement<>(qName, clazz, object);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            marshaller.marshal(root, outputStream);
            String marshalledString = new String(outputStream.toByteArray());

            return marshalledString;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

}
