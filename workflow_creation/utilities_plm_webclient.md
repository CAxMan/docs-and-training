# Using the PLM web client

## List of models

If PLM web client is launched via workflow, no extra sign-in is required. Workflow passes session token to PLM web client.
When PLM client starts the list of available repositories and models are presented in the left side panel. An example of available product structures is shown below.
There is only one repository *InitialRepository* and one single product structure (model) *Ultralight_Glider* in it.

<p align="center">
  <img src="img_plm/repositories_models.png"
   alt="Repositories and models" />
</p>

Click icon <img src="img_plm/open_icon.png" /> near to the model name of your interest, and a context menu with the single item "Open" will appear.

<p align="center">
  <img src="img_plm/model_open.png"
   alt="Context menu to open model" />
</p>

## Product structure and meta-data

The product structure of a model will be shown on the left side of the web page where repositories and projects were listed before.
Panel with repositories and models is collapsed to left side. But it can be shown again to switch to other product structure.

<p align="center">
  <img src="img_plm/project_structure_tree.png"
   alt="Project structure. Tree view" />
</p>

Children nodes are shown (expanded) by clicking plus sign or by clicking on node name.
Meta-data of a product structure node is opened from the left panel by clicking the <img src="img_plm/open_icon.png" /> icon and selecting "Open" menu item from the context menu. The result is depicted in the following picture.
By default tab "Files" is selected. And files attached to the selected folder are listed.

<p align="center">
  <img src="img_plm/opened_folder.png"
   alt="Opened folder with files" />
</p>

Product structure folders may have assigned user-defined property values.

<p align="center">
  <img src="img_plm/assigned_properties.png"
   alt="Assigned properties" />
</p>

## File upload

Files can be uploaded to a server and attached to a selected folder.
Click <img src="img_plm/open_icon.png" /> icon of your favorite node and select the "Add file" menu item from the context menu.
The following form will be shown.

<p align="center">
  <img src="img_plm/upload_file_form.png"
   alt="Form to upload a file" />
</p>

A file is selected by clicking the "Browse..." button. Select file type from the "Type" drop-down list. The description of the file is mandatory.
Click "OK" to start upload. After a while the uploaded file will appear in the "Files" tab.

## Export ZIP package

The content of a folder and all children below including their documents can be exported as a ZIP package.
Click <img src="img_plm/open_icon.png" /> icon of a required node and select the "Download" menu item from the context menu.

## Add remark

A folder can be commented, that is, a new remark may be added.
Click   icon of a folder and then select the "Add remark" menu item from the context menu.

<p align="center">
  <img src="img_plm/add_remark.png"
   alt="Form to add a remark" />
</p>

Give the subject for your remark and the remark itself. Then click "OK".

## Add approval

A folder and all its underlying content can be approved (disapproved) by adding an approval tag to the folder.
Click <img src="img_plm/open_icon.png" /> icon of a folder and then select the "Add approval" menu item from the context menu.

<p align="center">
  <img src="img_plm/add_approval.png"
   alt="Form to add an approval" />
</p>

Select the resolution type (approved, rejected or not applicable) and add a description.
Then click "OK".

## Import folder (ZIP package)

A local folder and all its content (including sub folders) can be imported to a selected node. This, however, requires that the local folder has been zipped first.
Click <img src="img_plm/open_icon.png" /> icon of a node and then select the "Import folder" menu item from the context menu.

<p align="center">
  <img src="img_plm/import_folder.png"
   alt="Form to import folder" />
</p>

Select "Folders type" item. All imported folder will be assigned this type.
"Files types" may be empty. In this case the system will set the file type (for each file) according to the individual file extension.

## Delete folder

A folder can be deleted. That is, it can be removed from the product structure.
Click <img src="img_plm/open_icon.png" /> icon of a node and then select the "Delete" menu item from the context menu.

<p align="center">
  <img src="img_plm/delete_confirmation.png"
   alt="Delete confirmation in Mozilla Firefox" />
</p>

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

## PLM web client as file chooser

Click icon <img src="img_plm/open_icon.png" /> near to the file name of your interest, and context menu will appear.

<p align="center">
  <img src="img_plm/context_menu_files.png"
   alt="Context menu on files" />
</p>

When you click "Select File" menu item, PLM web client is closed. Service dysplayHtml passes GSS identifier of selected file to the next workflow step.
