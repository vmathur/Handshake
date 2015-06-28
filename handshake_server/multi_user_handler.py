from tornado.ioloop import IOLoop
from tornado.web import RequestHandler, Application, url
import pycps
import json

class MultiUserHandler(RequestHandler):
	def get(self, id):
		con = pycps.Connection('tcp://cloud-us-0.clusterpoint.com:9007', 'users', 'vmmonkey@gmail.com', 'monkey', '100642')
		try:
			res = con.retrieve(id)
			user = res.get_documents().items()[0]
		except pycps.APIError as e:
			print(e)
			if e.code == 2824:
				print("Requested non-existing id(s): {0}".format(', '.join(e.document_id)))
			return

		connections = user[1]['connections']
		profiles = []

		for connection in connections:
			try:
				response = con.search(bluetooth_address, connection)
				profiles.append(response)
			except pycps.APIError as e:
				print(e)

		self.write(profiles)
		self.finish()