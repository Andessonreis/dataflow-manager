document.addEventListener("DOMContentLoaded", () => {
  console.log("App carregado. Pronto para CRUD.");
  // Funções de CRUD de DataSource
  loadDataSources();
  setupDataSourceForm();

  // Funções de CRUD de Pipeline
  loadPipelines();
  setupPipelineForm();

  // Funções de Leitura de JobRun
  loadJobRuns();
  setupJobRunFilters();
});

const API_BASE_URL = "http://localhost:8080/api";

// ===============================================
// 🚀 DATA SOURCE 
// ===============================================

const dataSourceTableBody = document.getElementById("dataSourceTableBody");
const dataSourceForm = document.getElementById("dataSourceForm");
const dsFormTitle = document.getElementById("dsFormTitle");
const dsFormIdInput = document.getElementById("dsId");
const dsCancelButton = document.getElementById("dsCancel");

// (READ) Carrega todos os DataSources
async function loadDataSources() {
  const response = await fetch(`${API_BASE_URL}/data-sources`);
  const data = await response.json();
  dataSourceTableBody.innerHTML = ""; // Limpa a tabela

  data.forEach((ds) => {
    const row = `
      <tr>
        <td>${ds.name}</td>
        <td>${ds.type}</td>
        <td>${ds.connectionUri}</td>
        <td>
          <button onclick="editDataSource('${ds.id}')">Editar</button>
          <button onclick="deleteDataSource('${ds.id}')">Deletar</button>
        </td>
      </tr>
    `;
    dataSourceTableBody.innerHTML += row;
  });
}

// (CREATE/UPDATE) Configura o formulário
function setupDataSourceForm() {
  dataSourceForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = dsFormIdInput.value;
    const formData = new FormData(dataSourceForm);

    const data = {
      name: formData.get("dsName"),
      type: formData.get("dsType"),
      connectionUri: formData.get("dsConnectionUri"),
    };

    const method = id ? "PUT" : "POST";
    const url = id
      ? `${API_BASE_URL}/data-sources/${id}`
      : `${API_BASE_URL}/data-sources`;

    await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    resetDataSourceForm();
    loadDataSources();
    loadPipelines(); // <-- CORREÇÃO AQUI (Atualiza o dropdown de Pipeline)
  });

  dsCancelButton.addEventListener("click", resetDataSourceForm);
}

// (DELETE)
async function deleteDataSource(id) {
  if (confirm("Tem certeza que quer deletar este DataSource?")) {
    await fetch(`${API_BASE_URL}/data-sources/${id}`, { method: "DELETE" });
    loadDataSources();
    loadPipelines(); 
  }
}

// (Helper) Prepara o formulário para edição
async function editDataSource(id) {
  const response = await fetch(`${API_BASE_URL}/data-sources/${id}`);
  const ds = await response.json();

  dsFormTitle.innerText = "Editar DataSource";
  dsFormIdInput.value = ds.id;
  document.getElementById("dsName").value = ds.name;
  document.getElementById("dsType").value = ds.type;
  document.getElementById("dsConnectionUri").value = ds.connectionUri;
}

// (Helper) Limpa o formulário
function resetDataSourceForm() {
  dsFormTitle.innerText = "Novo DataSource";
  dataSourceForm.reset();
  dsFormIdInput.value = "";
}

// ===============================================
// 🚀 PIPELINE
// ===============================================

const pipelineTableBody = document.getElementById("pipelineTableBody");
const pipelineForm = document.getElementById("pipelineForm");
const plFormTitle = document.getElementById("plFormTitle");
const plFormIdInput = document.getElementById("plId");
const plCancelButton = document.getElementById("plCancel");
const plDataSourceSelect = document.getElementById("plSourceId");

// (READ) Carrega todos os Pipelines
async function loadPipelines() {
  // 1. Busca os DataSources para preencher o <select>
  const dsResponse = await fetch(`${API_BASE_URL}/data-sources`);
  const dataSources = await dsResponse.json();
  plDataSourceSelect.innerHTML = '<option value="">Selecione...</option>';
  dataSources.forEach((ds) => {
    plDataSourceSelect.innerHTML += `<option value="${ds.id}">${ds.name}</option>`;
  });

  // 2. Busca os Pipelines
  const plResponse = await fetch(`${API_BASE_URL}/pipelines`);
  const data = await plResponse.json();
  pipelineTableBody.innerHTML = ""; // Limpa a tabela

  data.forEach((pl) => {
    const row = `
      <tr>
        <td>${pl.name}</td>
        <td>${pl.schedule}</td>
        <td>${pl.status}</td>
        <td>${pl.sourceId.substring(0, 8)}...</td>
        <td>
          <button onclick="runPipeline('${pl.id}')">▶ Run</button>
          <button onclick="editPipeline('${pl.id}')">Editar</button>
          <button onclick="deletePipeline('${pl.id}')">Deletar</button>
        </td>
      </tr>
    `;
    pipelineTableBody.innerHTML += row;
  });
}

// (CREATE/UPDATE)
function setupPipelineForm() {
  pipelineForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = plFormIdInput.value;
    const formData = new FormData(pipelineForm);

    const data = {
      name: formData.get("plName"),
      schedule: formData.get("plSchedule"),
      status: formData.get("plStatus"),
      sourceId: formData.get("plSourceId"),
    };
    
    // Validação simples
    if (!data.sourceId) {
        alert("Por favor, selecione um DataSource.");
        return;
    }

    const method = id ? "PUT" : "POST";
    const url = id
      ? `${API_BASE_URL}/pipelines/${id}`
      : `${API_BASE_URL}/pipelines`;

    await fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });

    resetPipelineForm();
    loadPipelines();
  });

  plCancelButton.addEventListener("click", resetPipelineForm);
}

// (DELETE)
async function deletePipeline(id) {
  if (confirm("Tem certeza que quer deletar este Pipeline?")) {
    await fetch(`${API_BASE_URL}/pipelines/${id}`, { method: "DELETE" });
    loadPipelines();
    loadJobRuns(); // Recarrega os jobs
  }
}

// (Helper) Edição
async function editPipeline(id) {
  const response = await fetch(`${API_BASE_URL}/pipelines/${id}`);
  const pl = await response.json();

  plFormTitle.innerText = "Editar Pipeline";
  plFormIdInput.value = pl.id;
  document.getElementById("plName").value = pl.name;
  document.getElementById("plSchedule").value = pl.schedule;
  document.getElementById("plStatus").value = pl.status;
  document.getElementById("plSourceId").value = pl.sourceId;
}

// (Helper) Reset
function resetPipelineForm() {
  plFormTitle.innerText = "Novo Pipeline";
  pipelineForm.reset();
  plFormIdInput.value = "";
}

// (AÇÃO ESPECIAL: RUN)
async function runPipeline(id) {
  // Mostra um feedback imediato
  alert(`Executando pipeline ${id.substring(0,8)}... Por favor, aguarde.`);
  
  const response = await fetch(`${API_BASE_URL}/pipelines/${id}/run`, {
    method: "POST",
  });
  const jobRun = await response.json();

  // Atualiza a tabela de JobRuns
  loadJobRuns();

  // Mostra um alerta com o resultado
  alert(
    `Job ${jobRun.id} executado! Status: ${jobRun.status}\nLogs: ${jobRun.logs}`
  );
}

// ===============================================
// 🚀 JOB RUN (READ-ONLY + FILTRO)
// ===============================================

const jobRunTableBody = document.getElementById("jobRunTableBody");
const jobRunFilterForm = document.getElementById("jobRunFilterForm");

// (READ) Carrega todos os JobRuns (com filtros)
async function loadJobRuns() {
  const formData = new FormData(jobRunFilterForm);
  const pipelineId = formData.get("filterPipelineId");
  const status = formData.get("filterStatus");

  const params = new URLSearchParams();
  if (pipelineId) params.append("pipelineId", pipelineId);
  if (status) params.append("status", status);

  const response = await fetch(`${API_BASE_URL}/job-runs?${params.toString()}`);
  const data = await response.json();
  jobRunTableBody.innerHTML = ""; // Limpa a tabela

  data.forEach((job) => {
    const row = `
      <tr class="status-${job.status.toLowerCase()}">
        <td>${job.status}</td>
        <td>${job.pipelineId.substring(0, 8)}...</td>
        <td>${
          job.startTime ? new Date(job.startTime).toLocaleString() : "N/A"
        }</td>
        <td>${
          job.endTime ? new Date(job.endTime).toLocaleString() : "N/A"
        }</td>
        <td>${job.success}</td>
        <td><pre>${job.logs || "Sem logs"}</pre></td>
      </tr>
    `;
    jobRunTableBody.innerHTML += row;
  });
}

// (Helper) Configura os filtros
function setupJobRunFilters() {
  // Recarrega a tabela sempre que um filtro mudar
  jobRunFilterForm.addEventListener("change", loadJobRuns);
  jobRunFilterForm.addEventListener("submit", (e) => {
    e.preventDefault();
    loadJobRuns();
  });
}

// Expondo funções para o HTML (para os botões 'onclick')
window.editDataSource = editDataSource;
window.deleteDataSource = deleteDataSource;
window.editPipeline = editPipeline;
window.deletePipeline = deletePipeline;
window.runPipeline = runPipeline;