<?php
// Configurações do banco de dados
$servername = "localhost";
$username = "root";  // usuário padrão do XAMPP
$password = "";      // senha padrão do XAMPP (vazia)
$dbname = "wear_app_db";

// Recebe os dados enviados pelo app Android
$message = isset($_POST['message']) ? $_POST['message'] : '';

if (empty($message)) {
    echo json_encode(["status" => "error", "message" => "Nenhuma mensagem recebida"]);
    exit;
}

// Cria a conexão
$conn = new mysqli($servername, $username, $password, $dbname);

// Verifica a conexão
if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Falha na conexão: " . $conn->connect_error]);
    exit;
}

// Define o charset para UTF-8
$conn->set_charset("utf8");

// Prepara e executa a query para inserir os dados
$stmt = $conn->prepare("INSERT INTO smartwatch_messages (message, created_at) VALUES (?, NOW())");
$stmt->bind_param("s", $message);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Mensagem salva com sucesso"]);
} else {
    echo json_encode(["status" => "error", "message" => "Erro ao salvar: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>