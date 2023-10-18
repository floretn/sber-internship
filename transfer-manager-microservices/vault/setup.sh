## CONFIG LOCAL ENV
echo "[*] Config local environment..."
# shellcheck disable=SC2142
#alias vault='docker-compose exec vault vault "$@"'
export VAULT_ADDR=http://vault:8200

## INIT VAULT
echo "[*] Init vault..."

VAULT_INITIALIZED=$(vault status | grep Initialized | awk '{print substr($0, 13, length($0))}')
if [[ ${VAULT_INITIALIZED} == "false" ]]; then
  echo "Require initialization"
  vault operator init -address=${VAULT_ADDR} > ./_data/keys.txt
else
  echo "Don't require initialization"
fi

export VAULT_TOKEN=$(grep 'Initial Root Token:' ./_data/keys.txt | awk '{print substr($NF, 1, length($NF))}')

## UNSEAL VAULT
echo "[*] Unseal vault..."
vault operator unseal -address=${VAULT_ADDR} $(grep 'Key 1:' ./_data/keys.txt | awk '{print $NF}')
vault operator unseal -address=${VAULT_ADDR} $(grep 'Key 2:' ./_data/keys.txt | awk '{print $NF}')
vault operator unseal -address=${VAULT_ADDR} $(grep 'Key 3:' ./_data/keys.txt | awk '{print $NF}')

## AUTH
echo "[*] Auth..."
vault login ${VAULT_TOKEN}

## CREATE TOKEN
if [[ ${VAULT_INITIALIZED} == "false" ]]; then
  echo "Require new token for app"
  echo -n "VAULT_TOKEN=" > ./_data/token.env
  vault token create -ttl=9200000000 | grep token | head -1 | awk '{print substr($0, 6, length($0))}' | sed 's/ //g' >> ./_data/token.env
else
  echo "Don't require new token for app"
fi