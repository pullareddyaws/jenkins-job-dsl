@Grab ('org.yaml:snakeyaml:1.17')
import org.yaml.snakeyaml.Yaml
import hudson.model.*


//Function to search for all the files ending with .yaml or .yml
ArrayList searchYamlFiles(String dirPath) {
    ArrayList file_list = []
    File dir = new File(dirPath)
    if (dir .isDirectory()) {
        dir.eachFileRecurse { File file ->
            if (file .isFile() && (file.name.endsWith(".yaml") || file.name.endsWith(".yml"))) {
                file_list.add(file.absolutePath)
            }
        }
    } else {
        println("Error: $dirPath is not valid directory.")
    }
    return file_list
}

//get the current working directory
def cwd = hudson.model.Executor.currentExecutor().getCurrentWorkspace().absolutize()

pipline_file_list = searchYamlFiles(cwd.toString())

for (current_pipeline in pipline_file_list){

    println("Working on : "+current_pipeline)
    // parsing the yaml content
    parsed_job_config = new Yaml().load((current_pipeline as File).text)

    println("jobname is : "+parsed_job_config.job_name)

    job(parsed_job_config.job_name) {
        steps {
        shell('echo Hello World!')
        }
    }   
}