const NF = (() => {
  async function request(path, options = {}) {
    const config = {
      credentials: 'same-origin',
      headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
      ...options
    };
    if (config.body && typeof config.body !== 'string') config.body = JSON.stringify(config.body);
    const res = await fetch(path, config);
    let data = null;
    const text = await res.text();
    if (text) {
      try { data = JSON.parse(text); } catch { data = text; }
    }
    if (!res.ok) {
      const msg = data && data.message ? data.message : `HTTP ${res.status}`;
      throw new Error(msg);
    }
    return data;
  }
  async function me() {
    try { return await request('/api/auth/me'); } catch { return null; }
  }
  async function logout() {
    await request('/api/auth/logout', { method:'POST' });
    location.href = '/login';
  }
  function qs(name) { return new URLSearchParams(location.search).get(name); }
  function money(value) { if (value == null || value === '') return '-'; return 'Rp ' + Number(value).toLocaleString('id-ID'); }
  function dateTime(value) { if (!value) return '-'; return new Date(value).toLocaleString('id-ID', { dateStyle:'medium', timeStyle:'short' }); }
  function escape(s) { return String(s ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
  function flash(message, ok = true) {
    const el = document.querySelector('#flash');
    if (!el) return alert(message);
    el.className = `alert ${ok ? 'ok' : 'err'}`;
    el.textContent = message;
    el.classList.remove('hidden');
    setTimeout(() => el.classList.add('hidden'), 3500);
  }
  async function renderNav(active = '') {
    const user = await me();
    const nav = document.querySelector('#nav');
    if (!nav) return user;
    const isAdmin = user && user.role === 'ADMIN';
    nav.innerHTML = `
      <div class="nav-inner">
        <a class="logo" href="/dashboard">Nongkrong<span>Finder</span></a>
        <div class="menu">
          <a class="${active === 'dashboard' ? 'active' : ''}" href="/dashboard">Dashboard</a>
          <a class="${active === 'tempat' ? 'active' : ''}" href="/tempat">Tempat</a>
          <a class="${active === 'event' ? 'active' : ''}" href="/event">Event</a>
          ${user ? `<a class="${active === 'favorit' ? 'active' : ''}" href="/favorit">Favorit</a><a class="${active === 'profil' ? 'active' : ''}" href="/profil">Profil</a>` : ''}
          ${isAdmin ? `<a class="${active.startsWith('admin') ? 'active' : ''}" href="/admin/dashboard">Admin</a>` : ''}
          ${user ? `<span class="badge ${isAdmin ? 'admin' : 'user'}">${user.fotoProfil ? `<img class="avatar" src="${escape(user.fotoProfil)}" alt="Foto profil" onerror="this.style.display='none'">` : ''}${escape(user.nama)} · ${escape(user.role)}</span><button class="btn dark" id="logoutBtn">Logout</button>` : `<a class="btn primary" href="/login">Login</a><a class="btn ghost" href="/register">Register</a>`}
        </div>
      </div>`;
    const btn = document.querySelector('#logoutBtn');
    if (btn) btn.addEventListener('click', logout);
    return user;
  }
  async function guard(role = null) {
    const user = await me();
    if (!user) { location.href = '/login'; return null; }
    if (role && user.role !== role) { location.href = '/dashboard'; return null; }
    return user;
  }
  function formToObject(form) {
    const obj = Object.fromEntries(new FormData(form).entries());
    for (const key of Object.keys(obj)) {
      if (obj[key] === '') obj[key] = null;
    }
    return obj;
  }
  return { request, me, logout, qs, money, dateTime, escape, flash, renderNav, guard, formToObject };
})();
