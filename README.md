# WearCommunication: Sistema de Comunicação entre Smartwatch e Smartphone com Armazenamento em MySQL

## 📱 Visão Geral

WearCommunication é uma aplicação Android com comunicação em tempo real entre smartwach WearOS e smartphones Android, com persistência de dados em um banco de dados MySQL. O sistema recebe os dados do smartwatch e os transmite para o smartphone, que armazena em um banco de dados centralizado através de uma API RESTful.

## 🎯 Objetivos

O objetivo deste projeto foi conhecer e trabalhar com novas tecnologias de comunicação entre dispositivos móveis e vestiveis, numa atividade passada em sala de aula na matéria de "Computação móvel e vestível".


## 🔧 Tecnologias Utilizadas

### Frontend (Mobile e Wearable)
- **Kotlin** - Linguagem de programação moderna e concisa para Android
- **Jetpack Compose** - Framework declarativo para UI no aplicativo Wear OS
- **XML Layouts** - Para a UI do aplicativo móvel
- **Coroutines** - Para operações assíncronas e não bloqueantes
- **Wearable Data Layer API** - Para comunicação bidirecional entre os dois dispositivos
- **HttpURLConnection** - Para comunicação com o servidor backend

### Backend
- **PHP** - Para processar requisições e interagir com o banco de dados, a API
- **MySQL** - Sistema de gerenciamento de banco de dados relacional
- **XAMPP** - Ambiente de desenvolvimento PHP + MySQL
- **JSON** - Formato de troca de dados entre cliente e servidor

## 📊 Arquitetura

O sistema segue uma arquitetura em três camadas:

1. **Camada Wearable (Smartwatch)**
   - Captura dados através de interface do usuário
   - Utiliza MessageClient e DataClient para enviar informações ao smartphone
   - Implementa WearableListenerService para comunicação em background

2. **Camada Mobile (Smartphone)**
   - Atua como ponte entre o smartwatch e o servidor
   - Recebe e processa dados do smartwatch
   - Envia dados ao servidor via requisições HTTP
   - Implementa padrão Singleton para gerenciamento de banco de dados

3. **Camada de Servidor (XAMPP)**
   - Recebe requisições do smartphone
   - Processa e valida dados
   - Armazena informações no banco de dados MySQL
   - Retorna respostas em formato JSON

## 🔄 Fluxo de Dados

1. O usuário inicia uma ação no smartwatch (pressionando um botão)
2. O Wear OS captura os dados e timestamp
3. Os dados são enviados para o smartphone através da Wearable Data Layer API
4. O smartphone recebe os dados via WearableListenerService (mesmo em background)
5. O DatabaseManager prepara a requisição HTTP para o servidor
6. O servidor PHP recebe, valida e insere os dados no MySQL
7. A resposta do servidor é registrada nos logs do aplicativo
8. A interface do usuário no smartphone é atualizada com os dados recebidos

## 🛠️ Componentes Principais

### Aplicativo Smartwatch
- **MainActivity**: Interface principal com botão para envio de dados
- **WearMessageService**: Serviço em background para comunicação bidirecional

### Aplicativo Smartphone
- **MainActivity**: Interface para exibição de mensagens recebidas
- **PhoneMessageService**: Serviço para recebimento de mensagens em background
- **DatabaseManager**: Gerencia a comunicação com o servidor PHP (padrão Singleton)

### Servidor
- **save_message.php**: Script para receber, validar e salvar dados no MySQL
- **Banco de dados**: Tabela smartwatch_messages para armazenamento das mensagens

## 💻 Funcionalidades

- Comunicação bidirecional entre smartwatch e smartphone
- Envio de mensagens instantâneas e dados persistentes
- Armazenamento de dados em banco MySQL via API PHP
- Interface de usuário intuitiva em ambos os dispositivos
- Funcionamento em background com WearableListenerServices
- Manipulação de erros e feedback visual para o usuário

## 🚀 Instalação e Configuração

### Pré-requisitos
- Android Studio (versão mais recente)
- XAMPP (ou outro servidor Apache+MySQL)
- Dispositivo/emulador Android com API 24+
- Dispositivo/emulador Wear OS com API 28+

### Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/RennaSag/WearCommunication.git
```

2. Importe o projeto no Android Studio

3. Configure o banco de dados MySQL: algumas instruções e arquivos estão na pasta "XAMP", do repositório
   - Inicie o XAMPP (Apache e MySQL)
   - Execute o script `create_database.sql` no phpMyAdmin
   - Certifique-se de que o arquivo `save_message.php` está no diretório correto

4. Ajuste a URL do servidor no DatabaseManager conforme necessário

5. Execute os aplicativos mobile e wear em seus respectivos dispositivos/emuladores

## 📈 Possibilidades de Expansão

- Implementação de autenticação OAuth para comunicação segura com o servidor
- Adição de recursos de sincronização offline
- Integração com Firebase para notificações em tempo real
- Dashboard web para visualização dos dados coletados
- Suporte a múltiplos tipos de dados e sensores do smartwatch

## 📝 Notas de Implementação

- A comunicação entre dispositivos utiliza tanto MessageClient (para mensagens efêmeras) quanto DataClient (para dados persistentes)
- Os serviços WearableListenerService permitem o recebimento de dados mesmo quando o aplicativo não está em primeiro plano
- Coroutines são utilizadas para evitar bloqueio da thread principal durante operações de rede
- O projeto segue as melhores práticas de desenvolvimento Android, incluindo o uso de padrões de design como Singleton

---

Desenvolvido com ❤️ para demonstrar a integração entre Wear OS, Android e servidores externos.
