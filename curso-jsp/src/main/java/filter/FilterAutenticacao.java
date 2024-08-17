package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import conection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/principal/*" }) /* intercepta todas as requisições que vierem do projeto ou mapeamento */
public class FilterAutenticacao extends HttpFilter implements Filter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Connection connection;

	public FilterAutenticacao() {

	}

	// encerra os processos quando o servidor está parado
	// mataria os processos de conexão com o banco
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// intercepta as requisições e da respostas no sistema
	// tudo que fizer no sistema vai fazer por aqui
	// validação de autenticação
	// dar commit ou rollback de transação do banco
	// validar e fazer redirecionamento de páginas
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath(); // url que está sendo acessada

			/* validar se está logado senão redireciona para a tela de login */
			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("principal/ServletLogin")) { // não está
																											// logado
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?="+urlParaAutenticar);
				request.setAttribute("msg", "Por favor realize o login!");
				redireciona.forward(request, response);
			} 
			chain.doFilter(request, response);
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	// inicia os processos ou recursos quando servidor sobe o projeto
	// inicar a conexão com o banco
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}

}
