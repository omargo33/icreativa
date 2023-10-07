# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs

export M2_HOME=/opt/apache-maven-3.9.4
export M2=$M2_HOME/bin

export PATH=$M2:$PATH

export DB_RAMV_HOST="127.0.0.1"
export DB_RAMV_USER="postgres"
export DB_RAMV_PASSWORD="postgres"


