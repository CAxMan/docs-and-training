# Using the PLM web client

...

# File selection using the PLM web client
Workflows in CAxMan typically communicate with one of the provided cloud
storages. The interactions towards the files themselves are typically handled
through GSS (Generic Storage Services), but this requires that the services
already know where a file is stored or, alternatively, which folder it should
upload a file into. In order to provide a user friendly way to pick files and
folders that will be used in a workflow, the PLM web client can be used.

The PLM web client is meant to be used as a step in
CAxMan workflows, serving as a GUI towards the PLM storage solution offered
in the project. It also provides a user-friendly and natural way to obtain
unique GSS names which can be used as input to other services later in the
workflow chain.

## Integrating the PLM web client in a workflow
The displayHtml service is currently registered under the following URI:
```
http://www.caxman.eu/apps/jotne/IframeDisplay.owl#displayHtml_Service
```
Use this URI to add the service to your workflow.

The following screen shot shows the minimal connections you need to make to
the PLM file chooser service block:
<p align="center">
  <img src="img_plm/wf_editor.png"
   alt="Minimal connections made to the PLM file chooser service" />
</p>

The complete set of parameters is explained in the following table:

| Parameter name | Wiring required? | Description |
| -------------- | --------- | ----------- |
| `sessionToken` | yes | The session token used for authentication. Should be connected to the workflow input with the same name |
| `serviceID` | yes | the same as sessionToken |
| `extraParameters` | yes | Contains parameters such as the GSS location, must be wired to corresponding workflow input. |
| `pageTitle` | yes | Textual caption in web browser |
| `url` | yes | URL to PLM web client. Normally it is "https://caxman.clesgo.net/jotne/STM_web/" |
