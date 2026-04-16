import javax.swing.*;
import java.awt.*;

public class FormPrincipal extends JFrame {
    private String usuarioLogado = null;

    // MENUS PRINCIPAIS
    private final JMenuBar menuBar;
    private final JMenu menuSistema;

    // SEGURANÇA DEVE EXISTIR SOMENTE QUANDO LOGADO
    private JMenu menuSeguranca;         
    private JMenuItem itemUsuarios;  

    // ITENS DE MENU
    private final JMenuItem itemFechar;
    private final JMenuItem itemLogin;

    public FormPrincipal() {
        super("Levi Software");

        // FUNDO
        JPanel painelFundo = new JPanel() {
            Image img = new ImageIcon(getClass().getResource("/javasavir/JavaLogo.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        setLayout(new BorderLayout());
        add(painelFundo, BorderLayout.CENTER);

        // ------ MENU ------
        menuBar = new JMenuBar();

        menuSistema = new JMenu("Sistema");
        itemLogin = new JMenuItem("Logar");
        itemFechar = new JMenuItem("Fechar");

        menuSistema.add(itemLogin);
        menuSistema.addSeparator();
        menuSistema.add(itemFechar);

        menuBar.add(menuSistema);
        setJMenuBar(menuBar);

        // --- AÇÕES ---

        // LOGIN
        itemLogin.addActionListener((e) -> {
            if (usuarioLogado == null) {
                realizarLogin();
            } else {
                int op = JOptionPane.showConfirmDialog(
                        this,
                        "Deseja sair da sessão de \"" + usuarioLogado + "\"?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION);

                if (op == JOptionPane.YES_OPTION) {
                    usuarioLogado = null;
                    aplicarEstadoAutenticacao();
                    JOptionPane.showMessageDialog(this, "Sessão encerrada.");
                }
            }
        });

        itemFechar.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente sair?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) System.exit(0);
        });

        // ---- JANELA ----
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // INICIAL SEM LOGIN, SEM MENU DE SEGURANÇA
        aplicarEstadoAutenticacao();
        setVisible(true);
    }

    // MOSTRA/ESCONDE O MENU SEGURANÇA CONFORME ESTEJA LOGADO
    private void aplicarEstadoAutenticacao() {
        boolean logado = (usuarioLogado != null);
        itemLogin.setText(logado ? "Logout" : "Logar");

        if (logado) {
            // Cria uma vez
            if (menuSeguranca == null) {
                menuSeguranca = new JMenu("Segurança");
                itemUsuarios = new JMenuItem("Usuários");
                itemUsuarios.addActionListener(e -> JOptionPane.showMessageDialog(this,
                        "Abrir tela de gestão de Usuários...",
                        "Segurança > Usuários", JOptionPane.INFORMATION_MESSAGE));
                menuSeguranca.add(itemUsuarios);
            }

            if (!isMenuPresente(menuSeguranca)) {  
                menuBar.add(menuSeguranca);
            }
        } else {                                    
            if (isMenuPresente(menuSeguranca)) {
                menuBar.remove(menuSeguranca);
            }
        }

        menuBar.revalidate();  
        menuBar.repaint();
    }

    // Checa se um menu já está na barra
    private boolean isMenuPresente(JMenu menu) {    
        if (menu == null) return false;
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            if (menuBar.getMenu(i) == menu) return true;
        }
        return false;
    }

    // Abre o diálogo de login, e se ok, aplica estado logado
    private void realizarLogin() {                  
        LoginDialog dlg = new LoginDialog(this);
        dlg.setVisible(true);
        if (dlg.isAutenticado()) {
            this.usuarioLogado = dlg.getUsuario();
            aplicarEstadoAutenticacao();
            JOptionPane.showMessageDialog(this, "Bem vindo, " + usuarioLogado + "!");
        }
    }

    public static void main(String[] args) {       
        SwingUtilities.invokeLater(FormPrincipal::new);
    }
}