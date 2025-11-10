# testes-automatizados-codefolio

## Crie um arquivo '.env' na raíz do projeto.

## Abra o site para testes

## Abra a DevTools(F12)

## Vá até o console(digite "allow pasting" caso necessário)

## Copie esse código no console: 
```
const DB_NAME  = "firebaseLocalStorageDb";
const STORE    = "firebaseLocalStorage";

indexedDB.open(DB_NAME).onsuccess = e => {
  const db = e.target.result;
  const tx = db.transaction(STORE, "readonly");
  const store = tx.objectStore(STORE);

  store.getAll().onsuccess = ev => {
    // normalmente tem só 1 registro
    const rec = ev.target.result[0];

    // KEY
    console.log("FIREBASE_KEY=" + rec.fbase_key);

    // VALUE (string pura para .env)
    console.log("FIREBASE_VALUE=" + JSON.stringify(rec));
  }
}
```

## Pegue as variáveis e cole no .env, uma por vez, do jeito que estão
