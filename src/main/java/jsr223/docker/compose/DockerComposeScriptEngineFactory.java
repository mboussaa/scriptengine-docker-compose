/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package jsr223.docker.compose;

import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import jsr223.docker.compose.utils.DockerComposeVersionGetter;
import processbuilder.SingletonProcessBuilderFactory;
import processbuilder.utils.ProcessBuilderUtilities;


public class DockerComposeScriptEngineFactory implements ScriptEngineFactory {

    // Script engine parameters
    public static final String NAME = "docker-compose";

    private static final String ENGINE = NAME;

    private static final String ENGINE_VERSION = "0.3.0";

    private static final String LANGUAGE = "yaml";

    private final Map<String, Object> parameters = new HashMap<>();

    private ProcessBuilderUtilities processBuilderUtilities = new ProcessBuilderUtilities();

    private DockerComposeVersionGetter dockerComposeVersionGetter = new DockerComposeVersionGetter(processBuilderUtilities);

    public DockerComposeScriptEngineFactory() {
        parameters.put(ScriptEngine.NAME, NAME);
        parameters.put(ScriptEngine.ENGINE_VERSION, ENGINE_VERSION);
        parameters.put(ScriptEngine.LANGUAGE, LANGUAGE);
        parameters.put(ScriptEngine.ENGINE, ENGINE);
    }

    public DockerComposeScriptEngineFactory(ProcessBuilderUtilities processBuilderUtilities,
            DockerComposeVersionGetter dockerComposeVersionGetter) {
        this();
        if (processBuilderUtilities == null || dockerComposeVersionGetter == null) {
            throw new NullPointerException("processBuilderUtilities and dockerComposeVersionGetter must not be null");
        }
        this.processBuilderUtilities = processBuilderUtilities;
        this.dockerComposeVersionGetter = dockerComposeVersionGetter;

    }

    @Override
    public String getEngineName() {
        return (String) parameters.get(ScriptEngine.NAME);
    }

    @Override
    public String getEngineVersion() {
        return (String) parameters.get(ScriptEngine.ENGINE_VERSION);
    }

    @Override
    public List<String> getExtensions() {
        return Arrays.asList("yml", "yaml");
    }

    @Override
    public List<String> getMimeTypes() {
        return Collections.singletonList("text/yaml");
    }

    @Override
    public List<String> getNames() {
        return Arrays.asList(ENGINE, "fig");
    }

    @Override
    public String getLanguageName() {
        return (String) parameters.get(ScriptEngine.LANGUAGE);
    }

    @Override
    public String getLanguageVersion() {
        return dockerComposeVersionGetter.getDockerComposeVersion(SingletonProcessBuilderFactory.getInstance());
    }

    @Override
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public String getMethodCallSyntax(String obj, String m, String... args) {
        return null;
    }

    @Override
    public String getOutputStatement(String toDisplay) {
        return null;
    }

    @Override
    public String getProgram(String... statements) {
        return null;
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return new DockerComposeScriptEngine();
    }
}
