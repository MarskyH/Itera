export default class CookieService {
  /**
   * Recupera o valor de um cookie pela sua chave.
   *
   * @param {string} chave - O nome do cookie a ser recuperado.
   * @returns {string | undefined} O valor do cookie, ou undefined se não for encontrado.
   */
  get(chave: string): string | undefined {
    const stringCookie = document.cookie;

    if (!stringCookie) {
      return undefined; // Lida com string de cookie vazia
    }

    const cookies: Record<string, string> = {};
    stringCookie.split(';').forEach(cookie => {
      const [nome, valor] = cookie.split('=').map(c => c.trim());
      cookies[nome] = decodeURIComponent(valor); // Decodifica o valor corretamente
    });

    return cookies[chave];
  }

  /**
   * Define um cookie com uma determinada chave, valor e tempo de expiração.
   *
   * @param {string} chave - O nome do cookie a ser definido.
   * @param {string} valor - O valor a ser armazenado no cookie.
   * @param {number} exp - O tempo de expiração em minutos (opcional, padrão é cookie de sessão).
   */
  set(chave: string, valor: string, exp?: number): void {
    const data = new Date();
    data.setTime(data.getTime() + (exp ? exp * 60 * 1000 : 0)); // Converte para milissegundos
    const expira = 'expires=' + data.toUTCString();
    document.cookie = `${chave}=${encodeURIComponent(valor)};${expira};path=/`; // Codifica o valor para segurança
  }

  /**
   * Remove um cookie pela sua chave.
   *
   * @param {string} chave - O nome do cookie a ser removido.
   */
  remove(chave: string): void {
    document.cookie = `${chave}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/`;
  }
}