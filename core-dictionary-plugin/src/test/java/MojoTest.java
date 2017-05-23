import com.bivgroup.dbmodel.plugin.DictionaryMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * Created by bush on 07.11.2016.
 */
public class MojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testexport() throws Exception {
        File pom = getTestFile("src/test/resources/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        //final MavenProject project = new MavenProject();
        //project.setFile(pom);
        //final MavenSession session = newMavenSession(project);
        //final MojoExecution execution = newMojoExecution("buildmodule");
        //final DictionaryMojo mojo = (DictionaryMojo) this.lookupConfiguredMojo(session, execution);
        DictionaryMojo mojo= (DictionaryMojo) lookupMojo("buildmodule", pom);
        assertNotNull(mojo);
        mojo.execute();
    }

}
