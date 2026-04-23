package javasavir;

import javax.swing.*;
import java.awt.*; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDialog extends JDialog
{
    private JTextField txtUsuario; 
    private JPasswordField txtSenha; 

    private boolean autenticado = false;
    private String usuario; 

    public LoginDialog(Frame parent) // Construtor
    {
        super(parent, "Login", true); // Janela modal

        setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== Usuario =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        add(txtUsuario, gbc);

        // ===== Senha =====
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        txtSenha = new JPasswordField(15);
        add(txtSenha, gbc);

        // ===== Botões =====
        JButton btnLogin = criarBotao("Entrar");
        JButton btnCancelar = criarBotao("Cancelar");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(painelBotoes, gbc);

        // ===== AÇÕES =====
        btnLogin.addActionListener(e -> fazerLogin());

        btnCancelar.addActionListener(e -> {
            autenticado = false;
            dispose();
        });

        getRootPane().setDefaultButton(btnLogin);

        // ===== CONFIGURAÇÃO FINAL =====
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    // Método padrão para botões
    private JButton criarBotao(String texto)
    {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(110, 35));
        return btn;
    }

    // ===== LÓGICA DE LOGIN =====
    private void fazerLogin()
    {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtSenha.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "Informe usuario e senha.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Testa conexão
        if (!Conexao.testarConexao())
        {
            JOptionPane.showMessageDialog(
                this,
                "Nao foi possivel conectar ao banco de dados.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String sql = "SELECT nome FROM tb_usuarios WHERE login = ? AND senha = ? LIMIT 1";

        try
        {
            Connection conn = Conexao.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next())
            {
                autenticado = true;
                usuario = rs.getString("nome");

                JOptionPane.showMessageDialog(
                    this,
                    "Login realizado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
                );

                dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(
                    this,
                    "Usuario ou senha invalidos!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(
                this,
                "Erro ao validar login:\n" + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isAutenticado()
    {
        return autenticado;
    }

    public String getUsuario()
    {
        return usuario;
    }
}